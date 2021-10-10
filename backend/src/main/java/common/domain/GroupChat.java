package common.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "groupChat")
@Getter
@Setter
@ToString
public class GroupChat extends Chat {

    @Transient
    private String adminSessionsKey;
    private String groupName;

    public GroupChat(Integer newChatId, ArrayList<String> participants, String secureSessionKey, String groupName) {
        super(newChatId, participants);
        this.adminSessionsKey = secureSessionKey;
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupChat groupChat = (GroupChat) o;
        return Objects.equals(getChatId(), groupChat.getChatId());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
