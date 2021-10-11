package client.service;

import common.*;
import common.domain.*;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientActionService {

    @Autowired
    private Connection connection;

//    public List<Message> getAllMessagesByChatId() throws IOException {

    public void sendAction(Action clientAction) throws IOException, ClassNotFoundException {
        if (connection.isAuthenticated()) {
            connection.getOutputStream().writeObject(clientAction);
            connection.getOutputStream().flush();
        }
    }

    public void getAllMessagesByChatId() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID, "");
        this.sendAction(clientAction);
    }

    public void getAllUserChats() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_USER_CHATS, "");
        this.sendAction(clientAction);
    }

    public void getAllUsers() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_USERS, "");
        this.sendAction(clientAction);
    }

    public boolean changeChatId(final Integer newChatId) throws IOException {
        // TODO get all chats and check is new chat id is available
        connection.getSession().setCurrentChatId(newChatId);
        return true;
    }

    public void createGroup(final String groupName) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_GROUP,
                groupName);

        this.sendAction(clientAction);
    }

    public void deleteGroup(final String groupName) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.DELETE_GROUP,
                groupName);

        this.sendAction(clientAction);
    }

    public void createO2o(final String username) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_O2O,
                username);

        this.sendAction(clientAction);
    }

    public void addParticipantToGroup(final String username) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.ADD_PARTICIPANT_TO_GROUP,
                username);

        this.sendAction(clientAction);
    }

    public void removeParticipantFromGroup(final String username) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.REMOVE_PARTICIPANT_FROM_GROUP,
                username);

        this.sendAction(clientAction);
    }
}
