package common;

import lombok.Data;
import server.Session;

@Data
public class Message extends BatchEntity {

    private Session sender;
    private String text;
    private Timesta
}
