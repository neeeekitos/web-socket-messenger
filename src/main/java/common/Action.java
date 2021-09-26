package common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.Session;

@Data
@EqualsAndHashCode(callSuper=false)
public class Action extends BatchEntity {

    public enum ActionType {
        CREATE_GROUP("\\create_group"),
        DELETE_GROUP("\\delete_group"),
        ADD_PARTICIPANT_TO_GROUP("\\add_participant"),
        REMOVE_PARTICIPANT_FROM_GROUP("\\remove_participant"),
        CREATE_O2O("\\create_o2o"),
        EXIT("\\exit"),
        NONE("");

        private final String identifier;

        ActionType(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String toString() {
            return this.identifier;
        }

        public static ActionType getActionTypeByIdentifier(String identifier) {
            for(ActionType e : values()) {
                if(e.identifier.equals(identifier)) return e;
            }
            return NONE;
        }
    }

    private ActionType action;
    private String payload;

    public Action(Session sender, ActionType action, String payload) {
        this.setSender(sender);
        this.action = action;
        this.payload = payload;
    }
}
