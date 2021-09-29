/**
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import common.*;
import org.springframework.beans.factory.annotation.Autowired;
import server.utils.KeyGenerator;
import service.MessageService;

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

    @Autowired
    private MessageService messageService;

    private final Connection connection;
    Authentication authentication = null;


    ConnectionThread(Connection connection, Map<String, Connection> activeConnections, Map<Integer, Chat> activeChats) {
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
                                    outputStream.writeObject(clientMessage);
                                    outputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            // persist message in the database
                            messageService.saveMessage(clientMessage);
                        } else {
                        }

                    }
                    else if (((BatchEntity) objectMsg).getType() == BatchEntity.EntityType.ACTION)
                    {
                        Action clientAction = (Action) objectMsg;
                        System.out.println("[" + authentication.getUsername() + " - " +
                                clientAction.getAction().toString() + "]:");

                        boolean done = this.applyAction(clientAction);
                        if (done) System.out.println("Action was successfully processed");
                        else System.out.println("Action wasn't completed");

                        if (clientAction.getAction() == Action.ActionType.EXIT) {
                            activeConnections.remove(clientAction.getPayload());
                            connection.close();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in connexionThread:" + e);
        }
    }

    private boolean applyAction(Action action) {
         return switch (action.getAction()) {
            case CREATE_GROUP -> this.createGroup(action);
            case DELETE_GROUP -> this.deleteGroup(action);
            case ADD_PARTICIPANT_TO_GROUP -> this.addParticipantToGroup(action);
            case REMOVE_PARTICIPANT_FROM_GROUP -> this.removeParticipantFromGroup(action);
            case CREATE_O2O -> this.createO2o(action);
            default -> true;
        };
    }

    private boolean createGroup(Action clientAction) {
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
        return true;
    }

    private boolean deleteGroup(Action clientAction) {
        Chat removed = activeChats.remove(clientAction.getChatId());
        boolean result = removed != null;

        // TODO check if participant (its username) exists

        if (result)
            System.out.println("[Action completed] Group " + clientAction.getPayload());
        else
            System.out.println("[Action aborted] Group " + clientAction.getPayload() + " wasn't created ");

        return result;
    }

    private boolean addParticipantToGroup(Action clientAction) {
        Chat chat = activeChats.get(clientAction.getChatId());

        if (chat == null) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't added to the chat because the chat "
                    + clientAction.getChatId() + " doesn't exist");
            return false;
        }

        if (chat.getParticipantsUsernames().contains(clientAction.getPayload())) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't added to the chat "
                    + clientAction.getChatId() + " because participant has been already added to this chat");
            return false;
        }
        else {
            chat.getParticipantsUsernames().add(clientAction.getPayload());
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " was added to the chat "
                    + clientAction.getChatId());
            return true;
        }
    }

    private boolean removeParticipantFromGroup(Action clientAction) {
        Chat chat = activeChats.get(clientAction.getChatId());
        Group group = null;

        if (chat == null) {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't removed from the chat because the chat "
                    + clientAction.getChatId() + " doesn't exist");
            return false;
        }

        if (!(chat instanceof Group)) {
            System.out.println("[Action aborted] Chat is not a group");
            return false;
        }
        else
            group = (Group) chat;


        if (group.getAdminSessionsKey() == clientAction.getSender().getSecureSessionKey()) {
            System.out.println("[Action aborted] Admin can not quit the group");
            return false;
        }

        if (chat.getParticipantsUsernames().contains(clientAction.getPayload())) {
            chat.getParticipantsUsernames().remove(clientAction.getPayload());

            return true;
        }
        else {
            System.out.println("[Action completed] Participant "
                    + clientAction.getPayload() + " wasn't removed from the chat "
                    + clientAction.getChatId() + " because the participant doesn't exist in this chat");
            return false;
        }
    }

    private boolean createO2o(Action clientAction) {
        Integer newChatId = activeChats.size();
        ArrayList<String> participants = new ArrayList<>();
        participants.add(clientAction.getSender().getUsername());

        O2o one2one = new O2o(newChatId, participants);

        // check if one2one is already present
        if (activeChats.containsValue(one2one)) {
            System.out.println("[Action aborted] O2o " + clientAction.getPayload() + " already exists");
            return false;
        }

        activeChats.put(newChatId, one2one);
        System.out.println("[Action completed] O2o " + clientAction.getPayload() + " has been created");
        return true;
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