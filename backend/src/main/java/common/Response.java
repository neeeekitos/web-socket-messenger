package common;

import lombok.Data;
import lombok.NoArgsConstructor;
import server.Session;

@Data
@NoArgsConstructor
public class Response extends BatchEntity {

    boolean success;
    ErrorCode errorCode;

    public Response(Session sender, Integer chatId, boolean success, ErrorCode errorCode) {
        super(sender, chatId, EntityType.RESPONSE);
        this.success = success;
        this.errorCode = errorCode;
    }
}
