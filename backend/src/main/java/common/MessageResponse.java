package common;

import lombok.Data;
import server.Session;

@Data
public class MessageResponse extends Response {
    Message clientMessage;

    public MessageResponse(Session sender, Integer chatId, boolean success, ErrorCode errorCode, Message message) {
        super(sender, chatId, success, errorCode);
        this.clientMessage = message;
    }
}
