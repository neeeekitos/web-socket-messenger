package common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public abstract class Chat {

    Integer chatId;
    ArrayList<String> participantsUsernames;

    public void send() {}
}
