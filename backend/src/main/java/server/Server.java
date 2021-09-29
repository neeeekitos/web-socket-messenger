package server;

import common.Chat;
import common.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Server {

    private Map<String, Connection> activeConnections;
    private Map<Integer, Chat> activeChats;

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
        new Server().start(3000);
    }

    public Server() {
        activeChats = new HashMap<>();
        activeConnections = new HashMap<>();
    }

    public void start(int port) {
        System.out.println("starting msg server...");

        ServerSocket listenSocket;

        try {
            listenSocket = new ServerSocket(port);
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connection received from " + clientSocket.getInetAddress().getHostAddress());


                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                Connection connection = new Connection(
                        clientSocket,
                        new ObjectInputStream(clientSocket.getInputStream()),
                        oos,
                        new Session(), // creating empty session
                        false // not authenticated yet
                );

                new Thread(new ConnectionThread(connection, this.activeConnections, this.activeChats)).start();
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }
    }
}
