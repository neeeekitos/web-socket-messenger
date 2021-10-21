package client.service;

import client.SocketListener;
import common.*;
import common.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.utils.*;

import java.io.IOException;

import static client.ClientServer.AUTHENTICATION_ATTEMPTS;

@Service
public class ClientAuthenticationService {

    private static int authAttempts = 0;

    @Autowired
    private Connection connection;

    @Autowired
    private SocketListener socketListener;

    public void authenticate(String username) throws IOException, ClassNotFoundException {

        String sessionKey = KeyGenerator.generateRandomKey();
        if (authAttempts < AUTHENTICATION_ATTEMPTS) {
            System.out.println("Enter your username.");

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
                connection.getSession().setCurrentChatId(0);
                connection.setAuthenticated(true);
                System.out.println("[Client]: " + connection.getSession().getUsername()
                        + " : you're successfully authenticated");
                socketListener.setConnection(connection);
                socketListener.start();
            }
            authAttempts++;
        }
    }
}
