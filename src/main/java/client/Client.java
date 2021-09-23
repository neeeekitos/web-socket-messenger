/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package client;

import common.Authentication;
import common.Connection;
import server.Session;
import server.utils.KeyGenerator;

import java.io.*;
import java.net.*;



public class Client {

    private static final int AUTHENTICATION_ATTEMPTS = 10;


    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket = null;
        BufferedReader stdIn = null;

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
        String username = args[2];
        String sessionKey = KeyGenerator.generateRandomKey();
        while (authAttempts < AUTHENTICATION_ATTEMPTS) {

            // send authentication to the server
            Authentication authentication = new Authentication(username, sessionKey);
            connection.getOutputStream().writeObject(authentication);

            // wait for a server response with a secure session key
            Object objectAuth = connection.getInputStream().readObject();
            System.out.println("object received");
            if (objectAuth instanceof Authentication && !connection.isAuthenticated()) {
                authentication = (Authentication) objectAuth;

                // set a secure session key
                connection.getSession().setSecureSessionKey(authentication.getSessionKey());
                connection.setAuthenticated(true);
                System.out.println("Successfully authenticated user with username : "
                        + authentication.getUsername()
                        + " and session key : "
                        + authentication.getSessionKey());
                break;
            }
            authAttempts++;
        }
        if (!connection.isAuthenticated()) {
            System.err.println("Authentication failed");
            System.exit(1);
        }

        // stage 1 : Message handling
        String line;
        while (true) {
            line=stdIn.readLine();

            if (line.equals(".")) break;
        }
        stdIn.close();
        socket.close();
    }
}


