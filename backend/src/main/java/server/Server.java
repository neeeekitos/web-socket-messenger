package server;

import common.domain.Chat;
import common.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import server.service.GroupService;
import server.service.MessageService;
import server.service.O2oService;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Service
@ComponentScan("common")
@EntityScan("common")
public class Server {

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private O2oService o2oService;

    private Map<String, Connection> activeConnections = new HashMap<>();
    private Map<Integer, Chat> activeChats = new HashMap<>();

    // start listening method
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        this.start(3000);
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
                new Thread(new ConnectionThread(connection, this.activeConnections, this.activeChats, messageService, groupService, o2oService)).start();
                //new Thread((Runnable) context.getBean("connectionThread", connection, this.activeConnections, this.activeChats)).start();
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }
    }
}
