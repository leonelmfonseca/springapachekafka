package com.developer.simpledemo.springapachekafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class SpringapachekafkaApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringapachekafkaApplication.class, args);
  }
}
