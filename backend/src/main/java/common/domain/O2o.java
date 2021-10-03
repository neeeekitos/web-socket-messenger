package common.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "o2o")
@Getter
@Setter
@ToString
public class O2o extends Chat {

    public O2o(Integer newChatId, ArrayList<String> participants) {
        super(newChatId, participants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        O2o o2o = (O2o) o;
        return Objects.equals(getChatId(), o2o.getChatId());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
