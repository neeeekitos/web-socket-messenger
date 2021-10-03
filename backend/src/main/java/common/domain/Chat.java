package common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chat")
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Chat {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer chatId;

    @ElementCollection
    private List<String> participantsUsernames;

    public Chat() {
        chatId = 0;
        participantsUsernames = new ArrayList<>();
    }

    public Chat(Integer newChatId, ArrayList<String> participants) {
        this.chatId = newChatId;
        this.participantsUsernames = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
