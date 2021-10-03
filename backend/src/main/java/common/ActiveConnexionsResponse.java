package common;

import common.domain.Response;
import server.Session;

import java.util.Map;

public class ActiveConnexionsResponse extends Response {

    Map<String, Connection> activeConnections;

    public ActiveConnexionsResponse(Session sender, Integer chatId, EntityType entityType, boolean actionSucceed, ErrorCode errorCode, Map<String, Connection> activeConnections) {
        super(sender, chatId, entityType, actionSucceed, errorCode);
        this.activeConnections = activeConnections;
    }
}
