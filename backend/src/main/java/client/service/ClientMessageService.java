package client.service;

import common.Action;
import common.Connection;
import common.domain.Message;
import common.domain.User;
import server.dao.MessageRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ClientMessageService {

    @Autowired
    private ClientActionService clientActionService;

    @Autowired
    private Connection connection;

    public List<Message> getAllMessagesByChatId() throws IOException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        return null;
    }

//    @Autowired
//    private MessageRepository messageRepository;
//
//    public Optional<Message> getMessage(final Long id) {
//        return messageRepository.findById(id);
//    }
//
//    public List<Message> getAllMessagesByChatId(final Long id) {
//        return messageRepository.findAll();
//    }
//
//    public void deleteMessage(final Long id) {
//        messageRepository.deleteById(id);
//    }
//
//    public Message saveMessage(Message message) {
//        Message savedMessage = messageRepository.save(message);
//        return savedMessage;
//    }

}