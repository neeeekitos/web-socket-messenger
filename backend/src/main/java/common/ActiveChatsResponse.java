package common;

import common.domain.Chat;
import common.domain.Response;
import server.Session;

import java.util.Map;

public class ActiveChatsResponse extends Response {

    Map<Integer, Chat> activeChats;

    public ActiveChatsResponse(Session sender, EntityType entityType, boolean actionSucceed, ErrorCode errorCode, Map<Integer, Chat> activeChats) {
        super(sender, entityType, actionSucceed, errorCode);
        this.activeChats = activeChats;
    }
}
