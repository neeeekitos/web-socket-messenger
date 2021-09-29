package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import server.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "message")
public class Message extends BatchEntity {

    @Id
    @Column(name = "id", nullable = false)
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
}
