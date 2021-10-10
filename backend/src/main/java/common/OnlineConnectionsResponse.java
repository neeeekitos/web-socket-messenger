package common;

import common.domain.Response;
import server.Session;

import java.util.Map;

public class OnlineConnectionsResponse extends Response {

    Map<String, Connection> activeConnections;

    public OnlineConnectionsResponse(Session sender, boolean actionSucceed, ErrorCode errorCode, Map<String, Connection> activeConnections) {
        super(sender, actionSucceed, errorCode);
        this.activeConnections = activeConnections;
    }
}
