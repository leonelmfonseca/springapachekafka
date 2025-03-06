package com.developer.simpledemo.springapachekafka.receiver;

import com.developer.simpledemo.springapachekafka.message.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    
    private final MessageRepository messageRepository;
    
    @Autowired
    public MessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    
    @KafkaListener(topics = "${myapp.kafka.topic}", groupId = "xyz")
    public void consume(String message) {
        LOGGER.info("MESSAGE RECEIVED AT CONSUMER END -> " + message);
        messageRepository.addMessage(message);
    }
}
