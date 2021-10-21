/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package client;

import client.service.ClientActionService;
import client.service.ClientAuthenticationService;
import common.*;
import common.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import common.Session;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
//@ComponentScan("server")
//@EntityScan("server")
@ComponentScan("common")
public class ClientServer {

    @Autowired
    private Connection connection;

    @Autowired
    private SocketListener socketListener;

    @Autowired
    private ClientAuthenticationService clientAuthenticationService;

    @Autowired
    private ClientActionService clientActionService;

    public static final int AUTHENTICATION_ATTEMPTS = 10;

    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        try {
            System.out.println("Launching client server...");
            this.start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, ClassNotFoundException {

        Socket socket = null;
        BufferedReader stdIn = null;

        String[] args = new String[] {"localhost",
                "3000",
                "test"};

        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <Unique username>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            connection.setSocket(socket);
            connection.setOutputStream(new ObjectOutputStream(socket.getOutputStream()));
            connection.setInputStream(new ObjectInputStream(socket.getInputStream()));
            connection.setSession(new Session());
            connection.setAuthenticated(false);
            System.out.println("[Client server] Socket connection established");
            System.out.println(connection.toString());


        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        // stage 1 : Authentication
        String username;
        while (!connection.isAuthenticated()) {

            System.out.println("Enter your username.");
            username = stdIn.readLine();

            clientAuthenticationService.authenticate(username);
        }

        if (!connection.isAuthenticated()) {
            System.err.println("Authentication failed.");
            System.exit(1);
        }

        // stage 2 : Message handling
        String line;
        while (true) {
            //wait for user keyboard entries
            line = stdIn.readLine();
            if (line.charAt(0) == '\\') {
                Pattern pattern = Pattern.compile("\\s");
                Matcher matcher = pattern.matcher(line);
                boolean found = matcher.find();
                String payload;
                String command;
                if (found) {
                    payload = line.substring(matcher.end());
                    command = line.substring(0, matcher.start());
                } else {
                    payload = "";
                    command = line.substring(0);
                }

                // TODO change chatId
                Action clientAction = new Action(connection.getSession(), Action.ActionType.getActionTypeByIdentifier(command), payload);

                if (clientAction.getAction() == Action.ActionType.EXIT)
                    break;

                System.out.println("[Action] : Command:  " + command + ", Payload : " + payload);

                switch (Action.ActionType.getActionTypeByIdentifier(command)) {
                    case GET_ALL_MESSAGES_BY_CHAT_ID -> this.clientActionService.getAllMessagesByChatId();
                    case GET_ALL_USERS -> this.clientActionService.getAllUsers();
                    case GET_ALL_USER_CHATS -> this.clientActionService.getAllUserChats();
                    case CREATE_GROUP -> this.clientActionService.createGroup(payload);
                    case DELETE_GROUP -> this.clientActionService.deleteGroup(payload);
                    case CREATE_O2O -> this.clientActionService.createO2o(payload);
                    case ADD_PARTICIPANT_TO_GROUP -> this.clientActionService.addParticipantToGroup(payload);
                    case REMOVE_PARTICIPANT_FROM_GROUP -> this.clientActionService.removeParticipantFromGroup(payload);
                }

            } else if (line.charAt(0) == '-') {
                // set current chat id
                connection.getSession().setCurrentChatId(Integer.parseInt(line.substring(1)));
                System.out.println("[Chat changed] : new chat id is " +
                        connection.getSession().getCurrentChatId());
            } else if (connection.isAuthenticated()){
                //send user message to server
                // TODO change chatId
                Message clientMessage = new Message(connection.getSession(), line, new Timestamp(System.currentTimeMillis()), connection.getSession().getCurrentChatId());
                connection.getOutputStream().writeObject(clientMessage);
                connection.getOutputStream().flush();

            }
        }
        stdIn.close();
        socket.close();
        connection.close();
    }
}