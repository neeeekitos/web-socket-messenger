package client.controller;

import client.service.ClientMessageService;
import common.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    private final ClientMessageService clientMessageService;

    @Autowired
    public MessagesController(ClientMessageService clientMessageService) {
        this.clientMessageService = clientMessageService;
    }

    @GetMapping("/{chatId}")
    public List<Message> getMessagesByChatId(@PathVariable Long chatId) {
        System.out.println("Fetching messages for chat id = " + chatId);
        return new LinkedList<>();
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