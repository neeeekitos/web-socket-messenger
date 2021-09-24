package common;

import lombok.Data;
import server.Session;

import java.io.Serializable;

@Data
public abstract class BatchEntity implements Serializable {

    private Session sender;

    private String chatId;

    private EntityType type;

    public enum EntityType {
        ACTION,
        MESSAGE
    }
}
