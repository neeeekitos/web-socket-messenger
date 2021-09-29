package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;

@Data
@Entity
@NoArgsConstructor
@Table(name = "group")
public class Group extends Chat {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String adminSessionsKey;
    private String groupName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group(Integer newChatId, ArrayList<String> participants, String secureSessionKey, String groupName) {
        super(newChatId, participants);
        this.adminSessionsKey = secureSessionKey;
        this.groupName = groupName;
    }
}
