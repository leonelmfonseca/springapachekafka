package com.developer.simpledemo.springapachekafka.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${myapp.kafka.topic}")
  private String topic;

  @Autowired
  public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(String message) {
    LOGGER.info("MESSAGE SENT FROM PRODUCER END -> " + message);
    kafkaTemplate.send(topic, message);
  }
}
