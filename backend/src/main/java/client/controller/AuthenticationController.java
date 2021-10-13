package client.controller;

import client.service.ClientActionService;
import client.service.ClientAuthenticationService;
import common.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private ClientAuthenticationService clientAuthenticationService;
    private Connection connection;

    @Autowired
    public AuthenticationController(ClientAuthenticationService clientAuthenticationService, Connection connection) {
        this.clientAuthenticationService = clientAuthenticationService;
        this.connection = connection;
    }

    @PostMapping("/authenticate")
    public void authenticate(@RequestBody String username) {
        System.out.println(" [Controller] : Authenticating with username : " + username);

        try {
            clientAuthenticationService.authenticate(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/isAuthenticated")
    public boolean isAuthenticated() {
        return connection.isAuthenticated();
    }

    @GetMapping("/getUsername")
    public String getUsername() {
        return connection.getSession().getUsername();
    }
}
