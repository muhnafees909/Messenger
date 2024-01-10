package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /*Create Message */
    public ResponseEntity<?> createMessage(Message newMessage) {
        Optional<Account> postedByUser = accountRepository.findById(newMessage.getPosted_by());
        if(newMessage.getMessage_text() == ""
        || newMessage.getMessage_text().length() > 255
        || postedByUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Message");
        }
       
        return ResponseEntity.ok(messageRepository.save(newMessage));
    }

    /*Get All Messages */
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageRepository.findAll());
    }

    /*Get Message By Id */
    public ResponseEntity<Message> getMessageById(int message_id) {
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        return optionalMessage
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.ok().build());
    }

    /*Delete Message */
    public ResponseEntity<?> deleteMessage(int message_id) {
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if(optionalMessage.isPresent()) {
            messageRepository.deleteById(message_id);
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok().build();
    }

    /*Update Message */
    public ResponseEntity<?> updateMessage(int message_id, Map<String, String> requestBody) {
        String message_text = requestBody.get("message_text");

        Optional<Message> optionalMessage = messageRepository.findById(message_id);

        if(optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            if(message_text == null || message_text.trim().isEmpty()  || message_text.length() > 255) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            message.setMessage_text(message_text);
            messageRepository.save(message);
            return ResponseEntity.ok(1);
        } 
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /*Get All Messages From User */
    public ResponseEntity<?> getAllMessagesFromUser(int account_id) {
        List<Message> allMessages = messageRepository.findAll();
        List<Message> userMessages = new ArrayList<>();
        
        for(Message message : allMessages) {
            if(message.getPosted_by() == account_id) {
                userMessages.add(message);
            }
        }

        return ResponseEntity.ok(userMessages);
    }
}
