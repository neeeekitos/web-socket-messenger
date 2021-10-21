package common;

import common.domain.Message;
import common.domain.Response;
import lombok.Data;

import java.util.List;

@Data
public class AllMessagesByChatIdResponse extends Response {
    List<Message> messages;

    public AllMessagesByChatIdResponse(Session sender, boolean actionSucceed, ErrorCode errorCode, List<Message> messages) {
        super(EntityType.ALL_MESSAGES_BY_USER_RESPONSE, sender, actionSucceed, errorCode);
        this.messages = messages;
    }
}
