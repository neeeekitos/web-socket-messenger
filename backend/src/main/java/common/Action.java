package common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.Session;

@Data
public class Action extends BatchEntity {

    public enum ActionType {
        CREATE_GROUP("\\create_group"),
        DELETE_GROUP("\\delete_group"),
        ADD_PARTICIPANT_TO_GROUP("\\add_participant"),
        REMOVE_PARTICIPANT_FROM_GROUP("\\remove_participant"),
        CREATE_O2O("\\create_o2o"),
        GET_ALL_MESSAGES_BY_CHAT_ID("\\get_all_messages"),
        GET_ALL_USERS("\\get_all_users"),
        GET_ONLINE_USERS("\\get_online_users"),
        GET_ALL_USER_CHATS("\\get_all_user_chats"),
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
        super(sender, EntityType.ACTION);
        this.action = action;
        this.payload = payload;
    }
}
