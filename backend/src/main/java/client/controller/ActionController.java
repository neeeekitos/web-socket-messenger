package client.controller;

import client.service.ClientActionService;
import common.*;
import common.domain.Chat;
import common.domain.Message;
import common.domain.Response;
import common.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ActionController(ClientActionService clientActionService, SimpMessagingTemplate simpMessagingTemplate) {
        this.clientActionService = clientActionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/changeCurrentChat/{newChatId}")
    public ResponseEntity<String> changeCurrentChat(@PathVariable Integer newChatId) throws URISyntaxException, IOException {
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
    public void getAllUserChats(){
        System.out.println(" [Controller] : Fetching all user's chats");

        try {
            clientActionService.getAllUserChats();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/allUsers")
    public void getAllUsers() {
        System.out.println(" [Controller] : Fetching all users");

        try {
            clientActionService.getAllUsers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/createGroup")
    public void createGroup(@RequestBody String groupName) {
        System.out.println(" [Controller] : Creating group : " + groupName);

        try {
            clientActionService.createGroup(groupName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/deleteGroup")
    public void deleteGroup(@RequestBody String groupName) {
        System.out.println(" [Controller] : Deleting group : " + groupName);

        try {
            clientActionService.deleteGroup(groupName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/addParticipant")
    public void addParticipantToGroup(@RequestBody String username) {
        System.out.println(" [Controller] : Adding participant : " +
                username + " : " + " to the current group");

        try {
            clientActionService.addParticipantToGroup(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/removeParticipant")
    public void removeParticipantFromGroup(@RequestBody String username) {
        System.out.println(" [Controller] : Removing participant : " +
                username + " : " + " from the current group");

        try {
            clientActionService.removeParticipantFromGroup(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/createO2o")
    public void createO2o(@RequestBody String username) {
        System.out.println(" [Controller] : Creating o2o with : " + username);

        try {
            clientActionService.createO2o(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @MessageMapping("/test")
    @SendTo("/topic/public")
    public List<String> onAllUsersReceived(Object object) throws IOException, ClassNotFoundException {
        if (object instanceof byte[]) {
            ArrayList<String> emptyResponse =  new ArrayList<>();
            emptyResponse.add("Init");
            return emptyResponse;
        }

        AllUsersResponse response = (AllUsersResponse) object;
        this.simpMessagingTemplate.convertAndSend("/topic/public", response.getUsers());
        return response.getUsers();
    }

    @MessageMapping("/userChats")
    @SendTo("/topic/chats")
    public Map<Integer, Chat> onAllUserChatsReceived(Object object) throws IOException, ClassNotFoundException {
        if (object instanceof byte[])
            return new HashMap<>();

        AllUserChatsResponse response = (AllUserChatsResponse) object;
        this.simpMessagingTemplate.convertAndSend("/topic/chats", response.getActiveChats());

        return response.getActiveChats();
    }

    @MessageMapping("/allMessages")
    @SendTo("/topic/messages")
    public List<Message> onAllMessagesReceived(Object object) throws IOException, ClassNotFoundException {
        if (object instanceof byte[])
            return new ArrayList<>();

        AllMessagesByChatIdResponse response = (AllMessagesByChatIdResponse) object;
        this.simpMessagingTemplate.convertAndSend("/topic/messages", response.getMessages());
        return response.getMessages();
    }

    @MessageMapping("/allUsers")
    @SendTo("/topic/users")
    public String test() throws IOException, ClassNotFoundException {
        System.out.println("received from test!");
        return "hui";
    }

    @MessageMapping("/newMessage")
    @SendTo("/topic/newMessage")
    public MessageResponse onNewMessageReceived(Object object) {
        MessageResponse response = null;
        if (object instanceof MessageResponse) {
            response = (MessageResponse) object;
            this.simpMessagingTemplate.convertAndSend("/topic/newMessage", response);
        }
        return response;
    }
}
