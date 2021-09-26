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

    public Message(Session sender, String text, Timestamp time) {
        this.setSender(sender);
        this.text = text;
        this.time = time;
        this.setType(EntityType.MESSAGE);
    }
}
