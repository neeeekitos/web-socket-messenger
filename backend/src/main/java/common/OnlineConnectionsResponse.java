package common;

import common.domain.Response;

import java.util.Map;

public class OnlineConnectionsResponse extends Response {

    Map<String, Connection> activeConnections;

    public OnlineConnectionsResponse(Session sender, boolean actionSucceed, ErrorCode errorCode, Map<String, Connection> activeConnections) {
        super(EntityType.ONLINE_CONNECTIONS_RESPONSE, sender, actionSucceed, errorCode);
        this.activeConnections = activeConnections;
    }
}
