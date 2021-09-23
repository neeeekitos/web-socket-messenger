package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.Session;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Message extends BatchEntity {

    private Session sender;
    private String text;
    private Timestamp time;
}
