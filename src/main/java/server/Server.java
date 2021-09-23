package server;

import stream.ClientThread;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {

    private Map<String, Connection> activeConnections;
    private Map<String, Chat> activeChats;

    public void start(int port) {
        System.out.println("starting msg server...");

        ServerSocket listenSocket;

        try {
            listenSocket = new ServerSocket(port);
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connection received from " + clientSocket.getInetAddress().getHostAddress());


                Connection connection = new Connection(
                        clientSocket,
                        new ObjectInputStream(clientSocket.getInputStream()),
                        new ObjectOutputStream(clientSocket.getOutputStream()),
                        new Session(), // creating empty session
                        false // not authenticated yet
                );

                new ConnectionThread(connection).start();
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }
    }
}
