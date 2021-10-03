/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package client;

import common.*;
import server.Session;
import server.utils.KeyGenerator;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Client {

    private static final int AUTHENTICATION_ATTEMPTS = 10;


    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket = null;
        BufferedReader stdIn = null;

        //TODO : delete username param
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
            if (line.charAt(0) == '\\')
            {
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
                Action clientAction = new Action(connection.getSession(), 0, Action.ActionType.getActionTypeByIdentifier(command), payload);
                if (clientAction.getAction() == Action.ActionType.EXIT)
                {
                    break;
                } else {
                    //send user action to server
                    connection.getOutputStream().writeObject(clientAction);
                    connection.getOutputStream().flush();

                    Response response = (Response) connection.getInputStream().readObject();
                    System.out.println("[Action] : Command:  " + command + ", Payload : " + payload + " | Success : " + response.isSuccess());
                    if (!response.isSuccess()) {
                        printErrorMessage(response.getErrorCode());
                    }

                }
            } else {
                //send user message to server
                // TODO change chatId
                Message clientMessage = new Message(connection.getSession(), 0, line, new Timestamp(System.currentTimeMillis()));
                connection.getOutputStream().writeObject(clientMessage);
                connection.getOutputStream().flush();

                Object objectMessage = connection.getInputStream().readObject();
                if (((BatchEntity) objectMessage).getType() == BatchEntity.EntityType.RESPONSE)
                {
                    if (objectMessage instanceof MessageResponse)
                    {
                        MessageResponse response = (MessageResponse) objectMessage;
                        System.out.println("> [" + response.getClientMessage().getTime() + "]: " + response.getClientMessage().getText());
                    }

                    else if (objectMessage instanceof Response)
                    {
                        Response response = (Response) objectMessage;
                        if (!response.isSuccess()) printErrorMessage(response.getErrorCode());
                        else System.out.println("Success");
                    }
                }


                //TODO to remove
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

    private static void printErrorMessage(ErrorCode errorCode) {
        switch (errorCode) {
            case NONE -> {
            }
            case NO_CHAT_CREATED -> {
                System.err.println("You must create a chat before.");
                break;
            }
            case NO_MATCHING_CHAT -> {
                System.err.println("No matching chat was found.");
                break;
            }
            case PARTICIPANT_ALREADY_ADDED -> {
                System.err.println("Participant already added.");
                break;
            }
            case INTERNAL_ERROR -> {
                System.out.println("An internal error occurred on the server.");
                break;
            }
            case CAN_NOT_REMOVE_ITSELF -> {
                System.out.println("You can not remove yourself.");
                break;
            }
            case PARTICIPANT_NOT_EXIST_IN_CHAT -> {
                System.out.println("Participant does not exist in the chat");
                break;
            }
            case ONLY_ADMIN_CAN_REMOVE_PARTICIPANT -> {
                System.out.println("Only admin can remove participant.");
                break;
            }
            default -> {
                break;
            }
        }
    }
}


