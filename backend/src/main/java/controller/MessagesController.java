package controller;

import common.Message;
import dao.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    private final MessageRepository messageRepository;

    public MessagesController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable Long id) {
        return messageRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity createMessage(@RequestBody Message message) throws URISyntaxException {
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.created(new URI("/clients/" + savedMessage.getId())).body(savedMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMessage(@PathVariable Long id, @RequestBody Message message) {
        Message currentMessage = messageRepository.findById(id).orElseThrow(RuntimeException::new);
        currentMessage.setText(message.getText());
        currentMessage.setSender(message.getSender());
        currentMessage.setChatId(message.getChatId());
        currentMessage.setTime(message.getTime());
        currentMessage = messageRepository.save(message);

        return ResponseEntity.ok(currentMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        messageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}