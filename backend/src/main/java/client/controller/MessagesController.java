package client.controller;

import client.service.ClientActionService;
import client.service.ClientMessageService;
import common.Connection;
import common.MessageResponse;
import common.domain.Message;
import common.domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/messages")
public class MessagesController {

    private ClientMessageService clientMessageService;
    private ClientActionService clientActionService;

    @Autowired
    public MessagesController(ClientMessageService clientMessageService, ClientActionService clientActionService) {
        this.clientMessageService = clientMessageService;
        this.clientActionService = clientActionService;
    }

    @GetMapping("/getMessagesByChatId")
    public @ResponseBody List<Message> getAllMessagesByChatId(@RequestParam Integer chatId) {
        System.out.println(" [Controller] : Fetching messages from current chat");

        List<Message> messages = new ArrayList<>();
        try {
            messages = clientMessageService.getAllMessagesByChatId();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
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