package common.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "group")
@Getter
@Setter
@ToString
public class Group extends Chat {

    @Column(name="adminSessionsKey")
    private String adminSessionsKey;

    @Column(name="groupName")
    private String groupName;

    public Group(Integer newChatId, ArrayList<String> participants, String secureSessionKey, String groupName) {
        super(newChatId, participants);
        this.adminSessionsKey = secureSessionKey;
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;
        return Objects.equals(getChatId(), group.getChatId());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
