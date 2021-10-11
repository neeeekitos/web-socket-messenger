package common.domain;

import common.BatchEntity;
import common.ErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.Session;

@Data
@NoArgsConstructor
public class Response extends BatchEntity {

    boolean success;
    ErrorCode errorCode;

    public Response(EntityType responseType, Session sender, boolean success, ErrorCode errorCode) {
        super(sender, responseType);
        this.success = success;
        this.errorCode = errorCode;
    }
}
