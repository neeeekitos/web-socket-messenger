package client.controller;

import client.service.ClientActionService;
import client.service.ClientMessageService;
import common.AllUserChatsResponse;
import common.AllUsersResponse;
import common.Connection;
import common.MessageResponse;
import common.domain.Chat;
import common.domain.Message;
import common.domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/messages")
public class MessagesController {

    private ClientMessageService clientMessageService;
    private ClientActionService clientActionService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessagesController(ClientMessageService clientMessageService, ClientActionService clientActionService, SimpMessagingTemplate simpMessagingTemplate) {
        this.clientMessageService = clientMessageService;
        this.clientActionService = clientActionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/getMessagesByChatId")
    public void getAllMessagesByChatId(@RequestParam Integer chatId) {
        System.out.println(" [Controller] : Fetching messages from current chat");

        try {
            clientMessageService.getAllMessagesByChatId(chatId);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/sendMessage")
    public @ResponseBody Message sendMessage(@RequestBody String text) throws URISyntaxException {
        System.out.println(" [Controller] :Sending message from : " +
                clientMessageService.getConnection().getSession().getUsername() + " with text : " + text);

        Message message = new Message();
        try {
            message = clientMessageService.sendMessage(text);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return message;
    }
}