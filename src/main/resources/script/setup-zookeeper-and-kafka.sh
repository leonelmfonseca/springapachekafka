#!/bin/bash

# Function to stop and clean up containers
cleanup_containers() {
    echo "Stopping and removing Zookeeper and Kafka containers and pod..."
    sudo podman stop kafka || true && \
    sudo podman stop zookeeper || true && \
    sudo podman rm kafka || true && \
    sudo podman rm zookeeper || true && \
    sudo podman pod stop kafka-pod || true && \
    sudo podman pod rm -f kafka-pod || true
}

cleanup_containers kafka zookeeper

echo "Creating pod to lodging zookeeper and kafka..."
sudo podman pod create --name kafka-pod -p 2181:2181 -p 9092:9092

echo "Starting Zookeeper container..."
sudo podman run -d --name zookeeper --pod kafka-pod \
-e ALLOW_ANONYMOUS_LOGIN=yes \
-e ZOO_4LW_COMMANDS_WHITELIST=* \
docker.io/bitnami/zookeeper:latest

if [ $? -ne 0 ]; then
  echo "Failed to start Zookeeper container. Exiting..."
  exit 1
fi

echo "Waiting for Zookeeper to be ready..."
until sudo podman exec zookeeper /bin/sh -c 'echo ruok | nc -w 5 localhost 2181 | grep -q imok'; do
  echo "Waiting for Zookeeper to be online..."
  sleep 5
done
echo "Zookeeper is ready!"

echo "Starting Kafka container..."
sudo podman run -d --name kafka --pod kafka-pod \
-e KAFKA_CFG_ZOOKEEPER_CONNECT=localhost:2181 \
-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092 \
-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
-e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT \
-e KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
docker.io/bitnami/kafka:latest

# Step 1: Get the directory of the current script (setup-mysql.sh)
SCRIPT_DIR=$(dirname "$(realpath "$0")")

# Step 2: Navigate to the parent directory of scripts
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../../../../")

echo "Starting Spring Boot application..."
cd "$PROJECT_ROOT" || exit
./mvnw dependency:resolve clean compile -DskipTests install spring-boot:run

if [ $? -ne 0 ]; then
  echo "Spring Boot application failed to start. Exiting..."
  cleanup_containers kafka zookeeper
  exit 1
fi

cleanup_containers kafka zookeeper