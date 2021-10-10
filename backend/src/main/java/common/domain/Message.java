package common.domain;

import common.BatchEntity;
import lombok.*;
import org.hibernate.Hibernate;
import server.Session;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "message")
@Getter
@Setter
@ToString
public class Message extends BatchEntity {

    private String text;
    private Timestamp time;
    private Integer chatId;

    public Message(Session sender, String text, Timestamp time, Integer chatId) {
        super(sender, EntityType.MESSAGE);
        this.text = text;
        this.time = time;
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
