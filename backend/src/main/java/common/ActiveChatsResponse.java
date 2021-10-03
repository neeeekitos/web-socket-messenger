package common;

import server.Session;

import java.util.Map;

public class ActiveChatsResponse extends Response {

    Map<Integer, Chat> activeChats;

    public ActiveChatsResponse(Session sender, Integer chatId, boolean actionSucceed, ErrorCode errorCode, Map<Integer, Chat> activeChats) {
        super(sender, chatId, actionSucceed, errorCode);
        this.activeChats = activeChats;
    }
}
