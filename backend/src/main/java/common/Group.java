package common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

public class Group extends Chat {

    private String adminSessionsKey;
    private String groupName;

    public Group(Integer newChatId, ArrayList<String> participants, String secureSessionKey, String groupName) {
        super(newChatId, participants);
        this.adminSessionsKey = secureSessionKey;
        this.groupName = groupName;
    }

    public void send() {

    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void addParticipants() {
    }

    public void removeParticipant() {
    }
}
