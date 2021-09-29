package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;

@Entity
@NoArgsConstructor
@Table(name = "o2o")
public class O2o extends Chat {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public O2o(Integer newChatId, ArrayList<String> participants) {
        super(newChatId, participants);
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
