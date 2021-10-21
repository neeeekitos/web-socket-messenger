package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BatchEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Session sender;

    private EntityType type;

    public BatchEntity(Session sender, EntityType entityType) {
        this.sender = sender;
        this.type = entityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum EntityType {
        ACTION,
        MESSAGE,
        MESSAGE_RESPONSE,
        ACTION_RESPONSE,
        ALL_USERS_RESPONSE,
        ALL_USER_CHATS_RESPONSE,
        ALL_MESSAGES_BY_USER_RESPONSE,
        ONLINE_CONNECTIONS_RESPONSE
    }
}
