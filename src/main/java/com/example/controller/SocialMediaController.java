package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. 
 * The endpoints you will need can be found in readme.md as well as the test cases. You be required 
 * to use the @GET/POST/PUT/DELETE/etc Mapping annotations where applicable as well as the 
 * @Responsebody and @PathVariable annotations. You should refer to prior mini-project labs and 
 * lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /*Register Handler */
    @PostMapping("/register")
    public ResponseEntity<?> addUserHandler(@RequestBody Account newAccount) {
        return accountService.addUser(newAccount);
    }

    /*Login Handler */
    @PostMapping("/login")
    public ResponseEntity<?> loginHandler(@RequestBody Account account) {
        return accountService.login(account);
    }

    /*Create Message Handler */
    @PostMapping("/messages")
    public ResponseEntity<?> createMessageHandler(@RequestBody Message newMessage) {
        return messageService.createMessage(newMessage);
    }

    /*Get All Messages */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return messageService.getAllMessages();
    }

    /*Get Message By Id */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id) {
        return messageService.getMessageById(message_id);
    }

    /*Delete Message */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int message_id) {
        return messageService.deleteMessage(message_id);
    }

    /*Update Message */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessage(@PathVariable int message_id, @RequestBody Map<String, String> requestBody) {
        return messageService.updateMessage(message_id, requestBody);
    }

    /*Get All Messages From User */
    @GetMapping("/accounts/{account_id}/messages") 
    public ResponseEntity<?> getAllMessagesFromUser(@PathVariable int account_id) {
        return messageService.getAllMessagesFromUser(account_id);
    }
}
