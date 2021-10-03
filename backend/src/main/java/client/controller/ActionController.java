package client.controller;

import client.service.ClientActionService;
import common.domain.Chat;
import common.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/actions")
public class ActionController {

    private ClientActionService clientActionService;

    @Autowired
    public ActionController(ClientActionService clientActionService) {
        this.clientActionService = clientActionService;
    }

    @PostMapping("/chatgeCurrentChat")
    public ResponseEntity<String> changeCurrentChat(@RequestBody Integer newChatId) throws URISyntaxException, IOException {
        boolean isChanged = clientActionService.changeChatId(newChatId);
        if (isChanged) {
            return new ResponseEntity<>(
                    "Your current chat is " + newChatId,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Bad request",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allUserChats")
    public List<Chat> getAllUserChats(){
        System.out.println(" [Controller] : Fetching all user's chats");

        List<Chat> users = new ArrayList<>();
        try {
            users = clientActionService.getAllUserChats();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        System.out.println(" [Controller] : Fetching all users");

        List<User> users = new ArrayList<>();
        try {
            users = clientActionService.getAllUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @PostMapping("/createGroup")
    public ResponseEntity createGroup(@RequestBody String groupName) throws URISyntaxException, IOException {
        System.out.println(" [Controller] : Creating group : " + groupName);

        clientActionService.createGroup(groupName);
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return null;
    }

    @PostMapping("/deleteGroup")
    public ResponseEntity deleteGroup(@RequestBody String groupName) throws URISyntaxException, IOException {
        System.out.println(" [Controller] : Deleting group : " + groupName);

        clientActionService.deleteGroup(groupName);
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return null;
    }

    @PostMapping("/addParticipant")
    public ResponseEntity addParticipantToGroup(@RequestBody String username) throws URISyntaxException, IOException {
        System.out.println(" [Controller] : Adding participant : " +
                username + " : " + " to the current group");

        clientActionService.addParticipantToGroup(username);
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return null;
    }

    @PostMapping("/removeParticipant")
    public ResponseEntity removeParticipantFromGroup(@RequestBody String username) throws URISyntaxException, IOException {
        System.out.println(" [Controller] : Removing participant : " +
                username + " : " + " from the current group");

        clientActionService.removeParticipantFromGroup(username);
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return null;
    }

    @PostMapping("/createO2o")
    public ResponseEntity createO2o(@RequestBody String username) throws URISyntaxException, IOException {
        System.out.println(" [Controller] : Creating o2o with : " + username);

        clientActionService.createO2o(username);
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return null;
    }



}
