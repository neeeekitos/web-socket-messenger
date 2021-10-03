package common.domain;

import common.BatchEntity;
import common.ErrorCode;
import server.Session;

public class Response extends BatchEntity {

    boolean actionSucceed;
    ErrorCode errorCode;

    public Response(Session sender, EntityType entityType, boolean actionSucceed, ErrorCode errorCode) {
        super(sender, entityType);
        this.actionSucceed = actionSucceed;
        this.errorCode = errorCode;
    }
}
