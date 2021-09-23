/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import common.BatchEntity;
import io.github.cdimascio.dotenv.Dotenv;
import server.utils.KeyGenerator;

import java.io.ObjectOutputStream;

public class ConnectionThread extends Thread {

    private Connection connection;

    ConnectionThread(Connection connection) {
        this.connection = connection;
    }

    /**
     * receives a request from client then sends an echo to the client
     * @param connection the client socket
     **/
    public void run() {
        try {

            ObjectOutputStream messageOut = connection.getOutputStream();

            // Stage 1 : authentication
            while (true) {
                Object objectAuth = connection.getInputStream().readObject();
                if (objectAuth instanceof Authentication && !connection.isAuthenticated()) {
                    Authentication authentication = (Authentication) objectAuth;
                    connection.getSession().setUsername(authentication.getUsername());

                    // generate a secure session key
                    Dotenv dotenv = Dotenv.load();
                    String secureSessionKey = KeyGenerator.generateSessionKey(authentication.getUsername(), dotenv.get("SECRET"));
                    connection.getSession().setSecureSessionKey(secureSessionKey);

                    System.out.println("Successfully authenticated user with username : "
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
                if (objectMsg instanceof BatchEntity && connection.isAuthenticated()) {

                }
            }
        } catch (Exception e) {
            System.err.println("Error in server:" + e);
        }
    }

}