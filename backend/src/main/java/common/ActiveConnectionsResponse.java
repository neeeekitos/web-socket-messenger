package common;

import common.domain.Response;
import server.Session;

import java.util.Map;

public class ActiveConnectionsResponse extends Response {

    Map<String, Connection> activeConnections;

    public ActiveConnectionsResponse(Session sender, EntityType entityType, boolean actionSucceed, ErrorCode errorCode, Map<String, Connection> activeConnections) {
        super(sender, entityType, actionSucceed, errorCode);
        this.activeConnections = activeConnections;
    }
}
