package common;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Chat {

    Integer chatId;
    ArrayList<String> participantsUsernames;

    public Chat() {
        chatId = 0;
        participantsUsernames = new ArrayList<>();
    }
}
