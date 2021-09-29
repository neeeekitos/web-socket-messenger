package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import server.Session;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper=false)
public class Message extends BatchEntity {
    private String text;
    private Timestamp time;

    public Message(Session sender, Integer chatId, String text, Timestamp time) {
        super(sender, chatId, EntityType.MESSAGE);
        this.text = text;
        this.time = time;
    }
}
