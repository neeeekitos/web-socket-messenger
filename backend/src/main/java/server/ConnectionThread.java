/**
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import common.*;
import server.utils.KeyGenerator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

public class ConnectionThread implements Runnable {

    /**
     * Map with username and Connection
     */
    private final Map<String, Connection> activeConnections;
    /**
     * Map with chatId and Chat
     */
    private final Map<Integer, Chat> activeChats;

    private final Connection connection;
    Authentication authentication = null;


    ConnectionThread(Connection connection,  Map<String, Connection> activeConnections, Map<Integer, Chat> activeChats) {
        this.connection = connection;
        System.out.println("New thread started");
        this.activeChats = activeChats;
        this.activeConnections = activeConnections;
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
                            ArrayList<String> participants = activeChats.get(clientMessage.getChatId()).getParticipantsUsernames();

                            // check if the sender is in the chat
                            if (!participants.contains(clientMessage.getSender().getUsername()))
                                continue;

                            // send the message to each participant
                            participants.forEach(participant -> {
                                try {
                                    ObjectOutputStream outputStream = activeConnections.get(participant).getOutputStream();
                                    MessageResponse response = getMessageResponse(clientMessage, true, ErrorCode.NONE);
                                    outputStream.writeObject(response);
                                    outputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                        }

                    }
                    else if (((BatchEntity) objectMsg).getType() == BatchEntity.EntityType.ACTION)
                    {
                        Action clientAction = (Action) objectMsg;
                        System.out.println("[" + authentication.getUsername() + " - " +
                                clientAction.getAction().toString() + "]:");

                        Response response = this.applyAction(clientAction);
                        //TODO only print success or faile once (not in applyAction too !
                        if (response.isSuccess()) System.out.println("Action was successfully processed");
                        else System.out.println("Action wasn't completed");

                        if (clientAction.getAction() == Action.ActionType.EXIT) {
                            activeConnections.remove(clientAction.getPayload());
                            connection.close();
                            break;
                        }

                        if (activeChats.size() > 0) {
                            ArrayList<String> participants = activeChats.get(clientAction.getChatId()).getParticipantsUsernames();

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
                        } else {
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in connexionThread:" + e);
        }
    }

    private Response getResponse(Action clientAction, boolean success, ErrorCode errorCode) {
        return new Response(clientAction.getSender(), clientAction.getChatId(), success, errorCode);
    }

    private MessageResponse getMessageResponse(Message message, boolean success, ErrorCode errorCode) {
        return new MessageResponse(message.getSender(), message.getChatId(), success, errorCode, message);
    }

    private Response applyAction(Action action) {
         return switch (action.getAction()) {
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
        activeChats.put(newChatId, new Group(
                newChatId,
                participants,
                clientAction.getSender().getSecureSessionKey(),
                clientAction.getPayload()
        ));
        System.out.println("[Action completed] Group " + clientAction.getPayload());
        return getResponse(clientAction, true, ErrorCode.NONE);
    }

    private Response deleteGroup(Action clientAction) {
        Response response = null;
        Chat removed = activeChats.remove(clientAction.getChatId());
        if (removed != null)
        {
            response = getResponse(clientAction, true, ErrorCode.NONE);
        } else
        {
            response = getResponse(clientAction, false, ErrorCode.NO_MATCHING_CHAT);
        }

        // TODO check if participant (its username) exists

        if (response.isSuccess())
            System.out.println("[Action completed] Group " + clientAction.getPayload());
        else
            System.out.println("[Action aborted] Group " + clientAction.getPayload() + " wasn't created ");

        return response;
    }

    private Response addParticipantToGroup(Action clientAction) {
        Chat chat = activeChats.get(clientAction.getChatId());

        if (chat == null) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't added to the chat because the chat "
                    + clientAction.getChatId() + " doesn't exist");
            return getResponse(clientAction, false, ErrorCode.NO_CHAT_CREATED);

        }

        if (chat.getParticipantsUsernames().contains(clientAction.getPayload())) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't added to the chat "
                    + clientAction.getChatId() + " because participant has been already added to this chat");
            return getResponse(clientAction, false, ErrorCode.PARTICIPANT_ALREADY_ADDED);
        }
        else {
            chat.getParticipantsUsernames().add(clientAction.getPayload());
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " was added to the chat "
                    + clientAction.getChatId());
            return getResponse(clientAction, true, ErrorCode.NONE);
        }
    }

    private Response removeParticipantFromGroup(Action clientAction) {
        Chat chat = activeChats.get(clientAction.getChatId());
        Group group = null;

        if (chat == null) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't removed from the chat because the chat "
                    + clientAction.getChatId() + " doesn't exist");
            return getResponse(clientAction, false, ErrorCode.NO_MATCHING_CHAT);
        }

        if (!(chat instanceof Group)) {
            System.out.println("[Action aborted] Chat is not a group");
            return getResponse(clientAction, false, ErrorCode.INTERNAL_ERROR);
        }
        else
            group = (Group) chat;

        if (group.getAdminSessionsKey() != clientAction.getSender().getSecureSessionKey()) {
            System.out.println("[Action aborted] Only admin can do this action");
            return getResponse(clientAction, false, ErrorCode.ONLY_ADMIN_CAN_REMOVE_PARTICIPANT);
        }
        //payload sess
        if (clientAction.getPayload() == clientAction.getSender().getUsername()) {
            System.out.println("[Action aborted] User can not remove itself.");
            return getResponse(clientAction, false, ErrorCode.CAN_NOT_REMOVE_ITSELF);
        }


        if (chat.getParticipantsUsernames().contains(clientAction.getPayload())) {
            chat.getParticipantsUsernames().remove(clientAction.getPayload());

            return getResponse(clientAction, true, ErrorCode.NONE);
        }
        else {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't removed from the chat "
                    + clientAction.getChatId() + " because the participant doesn't exist in this chat");

            return getResponse(clientAction, false, ErrorCode.PARTICIPANT_NOT_EXIST_IN_CHAT);
        }
    }


    private Response createO2o(Action clientAction) {
        Integer newChatId = activeChats.size();
        ArrayList<String> participants = new ArrayList<>();
        participants.add(clientAction.getSender().getUsername());

        O2o one2one = new O2o(newChatId, participants);

        // check if one2one is already present
        if (activeChats.containsValue(one2one)) {
            System.out.println("[Action aborted] O2o " + clientAction.getPayload() + " already exists");
            return getResponse(clientAction, false, ErrorCode.PARTICIPANT_NOT_EXIST_IN_CHAT);
        }

        activeChats.put(newChatId, one2one);
        System.out.println("[Action completed] O2o " + clientAction.getPayload() + " has been created");
        return getResponse(clientAction, true, ErrorCode.NONE);
    }

    public boolean getConnectedUsers(Action action) {
        return true;
    }

    public Map<Integer, Chat> getActiveChats() {
        return activeChats;
    }

    public Map<String, Connection> getActiveConnections() {
        return activeConnections;
    }
}