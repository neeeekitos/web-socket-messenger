package client.service;

import common.*;
import common.domain.Message;
import common.domain.Response;
import common.domain.User;
import server.dao.MessageRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ClientMessageService {

    @Autowired
    private ClientActionService clientActionService;

    @Autowired
    private Connection connection;

    public void sendObject(Object object) throws IOException, ClassNotFoundException {
        if (connection.isAuthenticated()) {
            connection.getOutputStream().writeObject(object);
            connection.getOutputStream().flush();
        }
    }

    public void getAllMessagesByChatId(Integer chatId) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID, chatId.toString());

        //send user action to server
        sendObject(clientAction);
    }

    public Message sendMessage(String text) throws IOException, ClassNotFoundException {
        Message clientMessage = new Message(connection.getSession(), text, new Timestamp(System.currentTimeMillis()), connection.getSession().getCurrentChatId());

        //send user message to server
        sendObject(clientMessage);
        return clientMessage;
    }
}