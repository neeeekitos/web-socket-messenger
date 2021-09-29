package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.Session;

import java.io.Serializable;

@Data
@AllArgsConstructor
public abstract class BatchEntity implements Serializable {

    private Session sender;

    private Integer chatId;

    private EntityType type;

    public enum EntityType {
        ACTION,
        MESSAGE
    }
}
