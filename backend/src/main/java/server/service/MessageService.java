package server.service;

import common.domain.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import server.dao.MessageRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
@ComponentScan("server")
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Optional<Message> getMessage(final Long id) {
        return messageRepository.findById(id);
    }

    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    public void deleteMessage(final Long id) {
        messageRepository.deleteById(id);
    }

    public Message saveMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }

    public List<Message> getAllMessagesByChatId(final Integer chatId) {
        return messageRepository.findMessagesByChatIdOrderByTimeDesc(chatId);
    }


}