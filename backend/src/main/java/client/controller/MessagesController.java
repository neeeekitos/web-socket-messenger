package client.controller;

import client.service.ClientActionService;
import client.service.ClientMessageService;
import common.Connection;
import common.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    private ClientMessageService clientMessageService;
    private ClientActionService clientActionService;

    @Autowired
    public MessagesController(ClientMessageService clientMessageService, ClientActionService clientActionService) {
        this.clientMessageService = clientMessageService;
        this.clientActionService = clientActionService;
    }

    @GetMapping
    public List<Message> getAllMessagesByChatId() {
        System.out.println(" [Controller] : Fetching messages from current chat");

        List<Message> messages = new ArrayList<>();
        try {
            messages = clientMessageService.getAllMessagesByChatId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

/*    @PostMapping
    public ResponseEntity createMessage(@RequestBody Message message) throws URISyntaxException {
        Message savedMessage = clientMessageService.save(message);
        return ResponseEntity.created(new URI("/clients/" + savedMessage.getId())).body(savedMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMessage(@PathVariable Long id, @RequestBody Message message) {
        Message currentMessage = clientMessageService.findById(id).orElseThrow(RuntimeException::new);
        currentMessage.setText(message.getText());
        currentMessage.setSender(message.getSender());
        currentMessage.setChatId(message.getChatId());
        currentMessage.setTime(message.getTime());
        currentMessage = clientMessageService.save(message);

        return ResponseEntity.ok(currentMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        messageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }*/
}