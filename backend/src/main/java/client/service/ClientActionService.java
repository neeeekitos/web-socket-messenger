package client.service;

import common.*;
import common.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientActionService {

    @Autowired
    private Connection connection;

//    public List<Message> getAllMessagesByChatId() throws IOException {
    public List<Message> getAllMessagesByChatId() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        AllMessagesByChatIdResponse response = (AllMessagesByChatIdResponse) readServerResponse();

        return response.getMessages();
    }

    public Map<Integer, Chat> getAllUserChats() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_USER_CHATS, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        AllUserChatsResponse response = (AllUserChatsResponse) readServerResponse();

        return response.getActiveChats();
    }

    public ArrayList<String> getAllUsers() throws IOException, ClassNotFoundException {
        Action clientAction = new Action(connection.getSession(), Action.ActionType.GET_ALL_USERS, "");

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        AllUsersResponse response = (AllUsersResponse) readServerResponse();

        return response.getUsers();
    }

    public boolean changeChatId(final Integer newChatId) throws IOException {
        // TODO get all chats and check is new chat id is available
        connection.getSession().setCurrentChatId(newChatId);
        return true;
    }

    public Response createGroup(final String groupName) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_GROUP,
                groupName);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        Response response = readServerResponse();
        return response;
    }

    public Response deleteGroup(final String groupName) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.DELETE_GROUP,
                groupName);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        Response response = readServerResponse();
        return response;
    }

    public Response createO2o(final String username) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.CREATE_O2O,
                username);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        Response response = readServerResponse();
        return response;
    }

    public Response addParticipantToGroup(final String username) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.ADD_PARTICIPANT_TO_GROUP,
                username);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        Response response = readServerResponse();
        return response;
    }

    public Response removeParticipantFromGroup(final String username) throws IOException, ClassNotFoundException {
        Action clientAction = new Action(
                connection.getSession(),
                Action.ActionType.REMOVE_PARTICIPANT_FROM_GROUP,
                username);

        //send user action to server
        connection.getOutputStream().writeObject(clientAction);
        connection.getOutputStream().flush();

        Response response = readServerResponse();
        return response;
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
