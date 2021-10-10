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

    public List<Message> getAllMessagesByChatId() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        AllMessagesByChatIdResponse response = (AllMessagesByChatIdResponse) readServerResponse();

        return response.getMessages();
    }

    public Message sendMessage(String text) throws IOException, ClassNotFoundException {
        Message clientMessage = new Message(connection.getSession(), text, new Timestamp(System.currentTimeMillis()), connection.getSession().getCurrentChatId());
        connection.getOutputStream().writeObject(clientMessage);
        connection.getOutputStream().flush();

        // wait for server response
        MessageResponse response = (MessageResponse) readServerResponse();
        return response.getClientMessage();
    }

    public Response readServerResponse() throws IOException, ClassNotFoundException {
        // wait for server response
        Response response = (Response) connection.getInputStream().readObject();
        if (!response.isSuccess()) {
            System.out.println(response.getErrorCode().toString());
        }
        return response;
    }
}