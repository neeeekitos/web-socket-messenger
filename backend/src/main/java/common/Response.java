package common;

import server.Session;

public class Response extends BatchEntity {

    boolean actionSucceed;
    ErrorCode errorCode;

    public Response(Session sender, Integer chatId, EntityType entityType, boolean actionSucceed, ErrorCode errorCode) {
        super(sender, chatId, entityType);
        this.actionSucceed = actionSucceed;
        this.errorCode = errorCode;
    }
}
