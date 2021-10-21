package common;

import common.domain.Chat;
import common.domain.Response;
import lombok.Data;

import java.util.Map;

@Data
public class AllUserChatsResponse extends Response {

    Map<Integer, Chat> activeChats;

    public AllUserChatsResponse(Session sender, boolean actionSucceed, ErrorCode errorCode, Map<Integer, Chat> activeChats) {
        super(EntityType.ALL_USER_CHATS_RESPONSE, sender, actionSucceed, errorCode);
        this.activeChats = activeChats;
    }
}
