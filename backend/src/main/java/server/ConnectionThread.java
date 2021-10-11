/**
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import common.*;
import common.domain.*;
import server.service.GroupService;
import server.service.O2oService;
import server.service.UserService;
import server.utils.*;
import server.service.MessageService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class ConnectionThread implements Runnable {

    /**
     * Map with username and Connection
     */
    private final Map<String, Connection> activeConnections;
    /**
     * Map with chatId and Chat
     */
    private final Map<Integer, Chat> activeChats;

    private MessageService messageService;
    private GroupService groupService;
    private O2oService o2oService;
    private UserService userService;
    private final Connection connection;
    private Authentication authentication = null;


    public ConnectionThread(Connection connection, Map<String, Connection> activeConnections, Map<Integer, Chat> activeChats, MessageService messageService, GroupService groupService, O2oService o2oService, UserService userService) {
        this.connection = connection;
        System.out.println("New thread started");
        this.activeChats = activeChats;
        this.activeConnections = activeConnections;
        this.messageService = messageService;
        this.groupService = groupService;
        this.o2oService = o2oService;
        this.userService = userService;
    }

    /**
     * receives a request from client then sends an echo to the client
     **/
    public void run() {

        try {

            ObjectOutputStream messageOut = connection.getOutputStream();

            // Stage 1 : authentication
            while (true) {
                System.out.println("[ConnectionThread]: 1 TRUE=" + true + " ---- isClosed=" + connection.getSocket().isClosed());
                Object objectAuth = connection.getInputStream().readObject();
                if (objectAuth instanceof Authentication && !connection.isAuthenticated()) {
                    authentication = (Authentication) objectAuth;
                    connection.getSession().setUsername(authentication.getUsername());

                    // generate a secure session key
                    //Dotenv dotenv = Dotenv.load();
                    // TODO replace by dotenv
                    String secureSessionKey = KeyGenerator.generateSecureSessionKey(authentication.getUsername(), "secret");
                    connection.getSession().setSecureSessionKey(secureSessionKey);
                    connection.setAuthenticated(true);
                    activeConnections.put(authentication.getUsername(), connection);

                    System.out.println("[ConnectionThread]: Successfully authenticated user with username : "
                            + authentication.getUsername()
                            + " and session key : "
                            + authentication.getSessionKey());

                    messageOut.writeObject(authentication);
                    messageOut.flush();
                    break;
                }
            }

            // Stage 2 : messages handling
            while (true) {
                Object objectMsg = connection.getInputStream().readObject();
                System.out.println("[ConnectionThread]: BLOP");

                if (objectMsg instanceof BatchEntity && connection.isAuthenticated()) {
                    if (((BatchEntity) objectMsg).getType() == BatchEntity.EntityType.MESSAGE)
                    {
                        Message clientMessage = (Message) objectMsg;
                        System.out.println("[" + authentication.getUsername() + " - " +
                                clientMessage.getTime() + "]:" + clientMessage.getText());
                        if (activeChats.size() > 0) {
                            List<String> participants = activeChats.get(clientMessage.getSender().getCurrentChatId()).getParticipantsUsernames();

                            // check if the sender is in the chat
                            if (!participants.contains(clientMessage.getSender().getUsername()))
                                continue;

                            // send the message to each participant
                            participants.forEach(participant -> {
                                try {
                                    ObjectOutputStream outputStream = activeConnections.get(participant).getOutputStream();
                                    MessageResponse response = getMessageResponse(clientMessage, true, ErrorCode.NONE);
                                    outputStream.writeObject(response);
//                                    outputStream.writeObject(clientMessage);
                                    outputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            // persist message in the database
                            System.out.println(messageService.saveMessage(clientMessage).toString());
                        } else {
                        }

                    }
                    else if (((BatchEntity) objectMsg).getType() == BatchEntity.EntityType.ACTION)
                    {
                        Action clientAction = (Action) objectMsg;
                        System.out.println("[" + authentication.getUsername() + " - " +
                                clientAction.getAction().toString() + "]:");

//                        boolean done = this.applyAction(clientAction);
//                        if (done) System.out.println("Action was successfully processed");
                        Response response = this.applyAction(clientAction);
                        //TODO only print success or fail once (not in applyAction too !
                        if (response.isSuccess()) System.out.println("Action was successfully processed");
                        else System.out.println("Action wasn't completed");

                        if (clientAction.getAction() == Action.ActionType.EXIT) {
                            activeConnections.remove(clientAction.getPayload());
                            connection.close();
                            break;
                        }

                        if (clientAction.getAction() == Action.ActionType.GET_ALL_MESSAGES_BY_CHAT_ID ||
                            clientAction.getAction() == Action.ActionType.GET_ALL_USERS ||
                            clientAction.getAction() == Action.ActionType.GET_ONLINE_USERS ||
                            clientAction.getAction() == Action.ActionType.GET_ALL_USER_CHATS) {

                            try {
                                ObjectOutputStream outputStream = activeConnections.get(clientAction.getSender().getUsername()).getOutputStream();
                                outputStream.writeObject(response);
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }


                        if (activeChats.size() > 0) {
                            List<String> participants = activeChats.get(clientAction.getSender().getCurrentChatId()).getParticipantsUsernames();

                            // check if the sender is in the chat
                            if (!participants.contains(clientAction.getSender().getUsername()))
                                continue;

                            // send the message to each participant
                            participants.forEach(participant -> {
                                try {
                                    ObjectOutputStream outputStream = activeConnections.get(participant).getOutputStream();
                                    outputStream.writeObject(response);
                                    outputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in connexionThread:" + e);
        }
    }

    private Response getResponse(Action clientAction, boolean success, ErrorCode errorCode) {
        return new Response(BatchEntity.EntityType.ACTION_RESPONSE, clientAction.getSender(), success, errorCode);
    }

    private MessageResponse getMessageResponse(Message message, boolean success, ErrorCode errorCode) {
        return new MessageResponse(message.getSender(), success, errorCode, message);
    }

    private Response applyAction(Action action) {
         return switch (action.getAction()) {
            case GET_ALL_MESSAGES_BY_CHAT_ID -> this.getAllMessagesByChatId(action);
            case GET_ONLINE_USERS -> this.getConnectedUsersResponse(action);
            case GET_ALL_USERS -> this.getAllUsersResponse(action);
            case GET_ALL_USER_CHATS -> this.getAllUserChatsResponse(action);
            case CREATE_GROUP -> this.createGroup(action);
            case DELETE_GROUP -> this.deleteGroup(action);
            case ADD_PARTICIPANT_TO_GROUP -> this.addParticipantToGroup(action);
            case REMOVE_PARTICIPANT_FROM_GROUP -> this.removeParticipantFromGroup(action);
            case CREATE_O2O -> this.createO2o(action);
            default -> getResponse(action, false, ErrorCode.INTERNAL_ERROR);
        };
    }

    private Response createGroup(Action clientAction) {
        Integer newChatId = activeChats.size();

        ArrayList<String> participants = new ArrayList<>();
        // TODO check if participant (its username) exists
        participants.add(clientAction.getSender().getUsername());
        GroupChat group = new GroupChat(
                newChatId,
                participants,
                clientAction.getSender().getSecureSessionKey(),
                clientAction.getPayload()
        );
        activeChats.put(newChatId, group);
        groupService.saveGroup(group);
        System.out.println("[Action completed] Group " + clientAction.getPayload());
        return getResponse(clientAction, true, ErrorCode.NONE);
    }

    private Response deleteGroup(Action clientAction) {
        Response response;
        Chat removed = activeChats.remove(clientAction.getSender().getCurrentChatId());

        if (removed != null) {
            groupService.deleteGroup(removed.getId());
            response = getResponse(clientAction, true, ErrorCode.NONE);
        } else
            response = getResponse(clientAction, false, ErrorCode.NO_MATCHING_CHAT);

        // TODO check if participant (its username) exists

        if (response.isSuccess())
            System.out.println("[Action completed] Group " + clientAction.getPayload());
        else
            System.out.println("[Action aborted] Group " + clientAction.getPayload() + " wasn't created ");

        return response;
    }

    private Response addParticipantToGroup(Action clientAction) {
        Chat chat = activeChats.get(clientAction.getSender().getCurrentChatId());

        if (chat == null) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't added to the chat because the chat "
                    + clientAction.getSender().getCurrentChatId() + " doesn't exist");
            return getResponse(clientAction, false, ErrorCode.NO_CHAT_CREATED);
        }

        if (chat.getParticipantsUsernames().contains(clientAction.getPayload())) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't added to the chat "
                    + clientAction.getSender().getCurrentChatId() + " because participant has been already added to this chat");
            return getResponse(clientAction, false, ErrorCode.PARTICIPANT_ALREADY_ADDED);
        }
        else {
            chat.getParticipantsUsernames().add(clientAction.getPayload());
            groupService.addParticipantToGroup(clientAction.getPayload(), chat.getId());
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " was added to the chat "
                    + clientAction.getSender().getCurrentChatId());
            return getResponse(clientAction, true, ErrorCode.NONE);
        }
    }

    private Response removeParticipantFromGroup(Action clientAction) {
        Chat chat = activeChats.get(clientAction.getSender().getCurrentChatId());
        GroupChat groupChat = null;

        if (chat == null) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't removed from the chat because the chat "
                    + clientAction.getSender().getCurrentChatId() + " doesn't exist");
            return getResponse(clientAction, false, ErrorCode.NO_MATCHING_CHAT);
        }

        if (!(chat instanceof GroupChat)) {
            System.out.println("[Action aborted] Chat is not a group");
            return getResponse(clientAction, false, ErrorCode.INTERNAL_ERROR);
        }
        else
            groupChat = (GroupChat) chat;


        if (Objects.equals(groupChat.getAdminSessionsKey(), clientAction.getSender().getSecureSessionKey())) {
            System.out.println("[Action aborted] Only admin can do this action");
            return getResponse(clientAction, false, ErrorCode.ONLY_ADMIN_CAN_REMOVE_PARTICIPANT);
        }

        //payload session
        if (Objects.equals(clientAction.getPayload(), clientAction.getSender().getUsername())) {
            System.out.println("[Action aborted] User can not remove itself.");
            return getResponse(clientAction, false, ErrorCode.CAN_NOT_REMOVE_ITSELF);
        }

        if (chat.getParticipantsUsernames().contains(clientAction.getPayload())) {
            chat.getParticipantsUsernames().remove(clientAction.getPayload());
            groupService.removeParticipantFromGroup(clientAction.getPayload(), chat.getId());
            return getResponse(clientAction, true, ErrorCode.NONE);
        } else {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't removed from the chat "
                    + clientAction.getSender().getCurrentChatId() + " because the participant doesn't exist in this chat");
            return getResponse(clientAction, false, ErrorCode.PARTICIPANT_NOT_EXIST_IN_CHAT);
        }
    }

    private Response createO2o(Action clientAction) {
        Integer newChatId = activeChats.size();
        ArrayList<String> participants = new ArrayList<>();
        participants.add(clientAction.getSender().getUsername());

        // TODO check if user exists
        participants.add(clientAction.getPayload());

        O2o one2one = new O2o(newChatId, participants);

        // check if one2one is already present
        if (activeChats.containsValue(one2one)) {
            System.out.println("[Action aborted] O2o " + clientAction.getPayload() + " already exists");
            return getResponse(clientAction, false, ErrorCode.O2O_ALREADY_EXISTS);
        }

        activeChats.put(newChatId, one2one);
        o2oService.saveO2o(one2one);
        System.out.println("[Action completed] O2o " + clientAction.getPayload() + " has been created");
        return getResponse(clientAction, true, ErrorCode.NONE);
    }

    public Response getAllMessagesByChatId(Action clientAction) {
        // TODO add pages to list of messages
        List<Message> messages = messageService.getAllMessagesByChatId(Integer.parseInt(clientAction.getPayload()));
        return new AllMessagesByChatIdResponse(clientAction.getSender(), true, ErrorCode.NONE, messages);
    }

    public Response getAllUsersResponse(Action clientAction) {
        return new AllUsersResponse(clientAction.getSender(), true, ErrorCode.NONE, getAllUsers());
    }

    public Response getConnectedUsersResponse(Action clientAction) {
        return new OnlineConnectionsResponse(clientAction.getSender(), true, ErrorCode.NONE, getOnlineConnections());
    }

    public Response getAllUserChatsResponse(Action clientAction) {

        Map<Integer, Chat> userChats = new HashMap<>();

        for (var entry : activeChats.entrySet()) {
            Integer chatId = entry.getKey();
            Chat chat = entry.getValue();

            if (chat.getParticipantsUsernames().contains(clientAction.getSender().getUsername())) {
                userChats.put(chatId, chat);
            }
        }

        return new AllUserChatsResponse(clientAction.getSender(), true, ErrorCode.NONE, userChats);
    }

    public Map<String, Connection> getOnlineConnections() {
        return activeConnections;
    }

    public ArrayList<String> getAllUsers() {
        return new ArrayList<>(activeConnections.keySet());
    }
}