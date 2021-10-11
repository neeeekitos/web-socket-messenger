package common;

import common.domain.Message;
import common.domain.Response;
import lombok.Data;
import server.Session;

@Data
public class MessageResponse extends Response {
    Message clientMessage;

    public MessageResponse(Session sender, boolean success, ErrorCode errorCode, Message message) {
        super(EntityType.MESSAGE_RESPONSE, sender, success, errorCode);
        this.clientMessage = message;
    }
}
