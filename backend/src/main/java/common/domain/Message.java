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

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;
    private Timestamp time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message(Session sender, Integer chatId, String text, Timestamp time) {
        super(sender, chatId, EntityType.MESSAGE);
        this.text = text;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
