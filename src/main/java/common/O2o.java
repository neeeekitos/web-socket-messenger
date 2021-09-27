package common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

public class O2o extends Chat {

    public O2o(Integer newChatId, ArrayList<String> participants) {
        super(newChatId, participants);
    }

    public void send() {

    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            O2o one2one = (O2o) object;
            if (this.participantsUsernames.equals(one2one.getParticipantsUsernames())) result = true;
        }
        return result;
    }
}
