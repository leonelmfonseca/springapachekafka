package com.developer.simpledemo.springapachekafka.controller;

import com.developer.simpledemo.springapachekafka.message.repository.MessageRepository;
import com.developer.simpledemo.springapachekafka.sender.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KafkaRestController {

   private final MessageProducer producer;
   private final MessageRepository messageRepository;

      @Autowired
      public KafkaRestController(MessageProducer producer, MessageRepository messageRepository ) {
          this.producer = producer;
          this.messageRepository = messageRepository;
      }

   
      @GetMapping("/send")
      public String sendMsg(
              @RequestParam("msg") String message) {
          producer.sendMessage(message);
          return "" +"'+message +'" + " sent successfully!";
      }

      //Read all messages
      @GetMapping("/getAll")
      public String getAllMessages() {
          return messageRepository.getAllMessages() ;
      }

}
