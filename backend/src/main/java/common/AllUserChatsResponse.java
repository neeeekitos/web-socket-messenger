package common;

import common.domain.Chat;
import common.domain.Response;
import lombok.Data;
import server.Session;

import java.util.Map;

@Data
public class AllUserChatsResponse extends Response {

    Map<Integer, Chat> activeChats;

    public AllUserChatsResponse(Session sender, boolean actionSucceed, ErrorCode errorCode, Map<Integer, Chat> activeChats) {
        super(sender, actionSucceed, errorCode);
        this.activeChats = activeChats;
    }
}
