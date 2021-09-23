package common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BatchEntity implements Serializable {

    private String chatId;

    private EntityType type;

    public enum EntityType {
        ACTION,
        MESSAGE
    }
}
