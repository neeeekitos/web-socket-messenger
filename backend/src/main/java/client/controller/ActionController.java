package client.controller;

import client.service.ClientActionService;
import common.Connection;
import common.domain.Chat;
import common.domain.Response;
import common.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/actions")
public class ActionController {

    private ClientActionService clientActionService;

    @Autowired
    public ActionController(ClientActionService clientActionService) {
        this.clientActionService = clientActionService;
    }

    @PostMapping("/changeCurrentChat")
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
    public @ResponseBody Map<Integer, Chat> getAllUserChats(){
        System.out.println(" [Controller] : Fetching all user's chats");

        Map<Integer, Chat> users = new HashMap<>();
        try {
            users = clientActionService.getAllUserChats();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    @GetMapping("/allUsers")
    public @ResponseBody ArrayList<String> getAllUsers() {
        System.out.println(" [Controller] : Fetching all users");

        ArrayList<String> users = new ArrayList<>();
        try {
            users = clientActionService.getAllUsers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(users.toString());
        return users;
    }

    @PostMapping("/createGroup")
    public ResponseEntity<String> createGroup(@RequestBody String groupName) throws URISyntaxException {
        System.out.println(" [Controller] : Creating group : " + groupName);

        Response response = new Response();
        try {
            response = clientActionService.createGroup(groupName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return new ResponseEntity<>(
                response.getErrorCode().toString(),
                response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/deleteGroup")
    public ResponseEntity<String> deleteGroup(@RequestBody String groupName) throws URISyntaxException {
        System.out.println(" [Controller] : Deleting group : " + groupName);

        Response response = new Response();
        try {
            response = clientActionService.deleteGroup(groupName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return new ResponseEntity<>(
                response.getErrorCode().toString(),
                response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/addParticipant")
    public ResponseEntity<String> addParticipantToGroup(@RequestBody String username) throws URISyntaxException {
        System.out.println(" [Controller] : Adding participant : " +
                username + " : " + " to the current group");

        Response response = new Response();
        try {
            response = clientActionService.addParticipantToGroup(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return new ResponseEntity<>(
                response.getErrorCode().toString(),
                response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/removeParticipant")
    public ResponseEntity<String> removeParticipantFromGroup(@RequestBody String username) throws URISyntaxException {
        System.out.println(" [Controller] : Removing participant : " +
                username + " : " + " from the current group");

        Response response = new Response();
        try {
            response = clientActionService.removeParticipantFromGroup(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(
                response.getErrorCode().toString(),
                response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/createO2o")
    public ResponseEntity<String> createO2o(@RequestBody String username) throws URISyntaxException {
        System.out.println(" [Controller] : Creating o2o with : " + username);

        Response response = new Response();
        try {
            response = clientActionService.createO2o(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        return ResponseEntity.created(new URI("/actions/" + createdGroup.getId())).body(createdGroup);
        return new ResponseEntity<>(
                response.getErrorCode().toString(),
                response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }



}
