package common;

import server.Session;

import java.util.Map;

public class ActiveConnectionsResponse extends Response {

    Map<String, Connection> activeConnections;

    public ActiveConnectionsResponse(Session sender, Integer chatId, boolean actionSucceed, ErrorCode errorCode, Map<String, Connection> activeConnections) {
        super(sender, chatId, actionSucceed, errorCode);
        this.activeConnections = activeConnections;
    }
}
