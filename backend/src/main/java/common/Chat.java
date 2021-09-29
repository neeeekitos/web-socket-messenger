package common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public abstract class Chat {

    Integer chatId;
    ArrayList<String> participantsUsernames;

    private Chat() {
        chatId = 0;
        participantsUsernames = new ArrayList<>();
    }

    public void send() {}
}
