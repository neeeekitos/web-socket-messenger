package client.service;

import common.Action;
import common.Connection;
import common.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ClientActionService {

    @Autowired
    private Connection connection;

    public List<Message> getAllMessagesByChatId() throws IOException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        return null;
    }

    public List<Chat> getAllUserChats() throws IOException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_USER_CHATS, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        return null;
    }

    public List<User> getAllUsers() throws IOException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_USERS, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        return null;
    }

    public boolean changeChatId(final Integer newChatId) throws IOException {
        // TODO get all chats and check is new chat id is available
        connection.getSession().setCurrentChatId(newChatId);
        return true;
    }

    public void createGroup(final String groupName) throws IOException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_GROUP,
                groupName);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();
    }

    public void deleteGroup(final String groupName) throws IOException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.DELETE_GROUP,
                groupName);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();
    }

    public void createO2o(final String username) throws IOException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_O2O,
                username);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();
    }

    public void addParticipantToGroup(final String username) throws IOException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_O2O,
                username);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();
    }

    public void removeParticipantFromGroup(final String username) throws IOException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.REMOVE_PARTICIPANT_FROM_GROUP,
                username);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();
    }
}
