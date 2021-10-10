package client;

import client.service.ClientAuthenticationService;
import client.service.ClientMessageService;
import common.BatchEntity;
import common.Connection;
import common.MessageResponse;
import common.domain.Message;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Data
public class SocketListener extends Thread {

    @Autowired
    Connection connection;

    @Autowired
    private ClientAuthenticationService clientAuthenticationService;

    boolean isPaused = true;

    @SneakyThrows
    public void run() {
        while(true) {

            if (!isPaused) {
                Object objectMessage = connection.getInputStream().readObject();
                // TODO Change to Message type
                if (((BatchEntity) objectMessage).getType() == BatchEntity.EntityType.RESPONSE) {
                    Message clientMsg = ((MessageResponse) objectMessage).getClientMessage();
                    System.out.println("New message from chat : "
                            + clientMsg.getSender().getCurrentChatId()
                            + "\r\nText: "
                            + clientMsg.getText());

                }
            }
        }
    }
}
