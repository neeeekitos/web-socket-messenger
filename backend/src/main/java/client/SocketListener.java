package client;

import client.controller.ActionController;
import client.controller.MessagesController;
import client.service.ClientActionService;
import client.service.ClientAuthenticationService;
import client.service.ClientMessageService;
import common.Action;
import common.BatchEntity;
import common.Connection;
import common.MessageResponse;
import common.domain.Message;
import common.domain.Response;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
public class SocketListener extends Thread {

    @Autowired
    Connection connection;

    @Autowired
    private ActionController actionController;

    @Autowired
    private MessagesController messagesController;

    @SneakyThrows
    public void run() {
        while(true) {

            Object object = connection.getInputStream().readObject();

            switch (((BatchEntity) object).getType()) {
                case MESSAGE_RESPONSE -> this.onMessageReceived(object);
                case ACTION_RESPONSE -> this.onActionResponseReceived(object);
                case ALL_MESSAGES_BY_USER_RESPONSE -> this.actionController.onAllMessagesReceived(object);
                case ALL_USER_CHATS_RESPONSE -> this.actionController.onAllUserChatsReceived(object);
                case ALL_USERS_RESPONSE -> this.actionController.onAllUsersReceived(object);
//                case BatchEntity.EntityType.ONLINE_CONNECTIONS_RESPONSE -> this.clientActionService.onOnlineConnectionsReceived(object);
            }
        }
    }

    public void onMessageReceived(Object object) {
        this.actionController.onNewMessageReceived(object);
        Message clientMsg = ((MessageResponse) object).getClientMessage();
        System.out.println("New message from chat : "
                + clientMsg.getSender().getCurrentChatId()
                + "\r\nText: "
                + clientMsg.getText());
    }

    public void onActionResponseReceived(Object object) {
        Response actionResponse = (Response) object;
        if (actionResponse.isSuccess()) {
            System.out.println(actionResponse.getErrorCode().toString());
        }

    }
}
