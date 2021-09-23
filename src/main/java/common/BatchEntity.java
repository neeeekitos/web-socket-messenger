package common;

import lombok.Data;

@Data
public class BatchEntity {

    private String chatId;

    private EntityType type;

    public enum EntityType {
        ACTION,
        MESSAGE
    }
}
