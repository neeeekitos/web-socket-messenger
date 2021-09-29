/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package client;

import common.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.Session;
import server.utils.KeyGenerator;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootApplication
public class ClientServer {

    private static final int AUTHENTICATION_ATTEMPTS = 10;

    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SpringApplication.run(ClientServer.class, args);

        Socket socket = null;
        BufferedReader stdIn = null;
        Integer currentChat = 0;

        args = new String[] {"localhost",
                "3000",
                "test"};

        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <Unique username>");
            System.exit(1);
        }

        Connection connection = null;

        try {
            // creation socket ==> connexion
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            stdIn = new BufferedReader(new InputStreamReader(System.in));



            connection = new Connection(
                    socket,
                    new ObjectInputStream(socket.getInputStream()),
                    new ObjectOutputStream(socket.getOutputStream()),
                    new Session(), // creating empty session
                    false // not authenticated yet
            );

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        // stage 1 : Authentication
        int authAttempts = 0;
        String username;
        String sessionKey = KeyGenerator.generateRandomKey();
        while (authAttempts < AUTHENTICATION_ATTEMPTS) {
            System.out.println("Enter your username.");
            username = stdIn.readLine();

            // send authentication to the server
            Authentication authentication = new Authentication(username, sessionKey);
            connection.getOutputStream().writeObject(authentication);
            connection.getOutputStream().flush();

            // wait for a server response with a secure session key
            Object objectAuth = connection.getInputStream().readObject();
            if (objectAuth instanceof Authentication && !connection.isAuthenticated()) {
                authentication = (Authentication) objectAuth;

                // set a secure session key
                connection.getSession().setSecureSessionKey(authentication.getSessionKey());
                connection.getSession().setUsername(authentication.getUsername());
                connection.setAuthenticated(true);
                System.out.println("[Client]: " + connection.getSession().getUsername()
                        + " : you're successfully authenticated");
                break;
            }
            authAttempts++;
        }
        if (!connection.isAuthenticated()) {
            System.err.println("Authentication failed.");
            System.exit(1);
        }

        // stage 1 : Message handling
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

                System.out.println("[Action] : Command:  " + command + ", Payload : " + payload);

                // TODO change chatId
                Action clientAction = new Action(connection.getSession(), currentChat, Action.ActionType.getActionTypeByIdentifier(command), payload);
                if (clientAction.getAction() == Action.ActionType.EXIT) {
                    break;
                } else {
                    //send user action to server
                    connection.getOutputStream().writeObject(clientAction);
                    connection.getOutputStream().flush();
                }
            } else if (line.charAt(0) == '-') {
                // set current chat id
                currentChat = Integer.parseInt(line.substring(1));
                System.out.println("[Chat changed] : new chat id is " + currentChat);
            } else {
                //send user message to server
                // TODO change chatId
                Message clientMessage = new Message(connection.getSession(), currentChat, line, new Timestamp(System.currentTimeMillis()));
                connection.getOutputStream().writeObject(clientMessage);
                connection.getOutputStream().flush();

                Object objectMessage = connection.getInputStream().readObject();
                if (((BatchEntity) objectMessage).getType() == BatchEntity.EntityType.MESSAGE)
                {
                    Message clientMsg = (Message) objectMessage;
                    System.out.println("New message from chat : "
                            + clientMsg.getChatId()
                            + "\r\nText: "
                            + clientMsg.getText());

                }
            }


        }
        stdIn.close();
        socket.close();
        connection.close();
    }
}


