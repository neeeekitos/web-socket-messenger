package common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.Session;

@Data
@EqualsAndHashCode(callSuper=false)
public class Action extends BatchEntity {

    public enum ActionType {
        CREATE_GROUP,
        DELETE_GROUP,
        ADD_PARTICIPANT_TO_GROUP,
        REMOVE_PARTICIPANT_FROM_GROUP,
        CREATE_O2O,
        EXIT,
        NONE
    }

    private ActionType action;

    public Action(Session sender, ActionType action) {
        this.setSender(sender);
        this.action = action;
    }

    public static ActionType parseActionStringIntoActionType(String actionString)
    {
        switch (actionString)
        {
            case "\\create_group":
                return ActionType.CREATE_GROUP;
            case "\\delete_group":
                return ActionType.DELETE_GROUP;
            case "\\add_participant":
                return ActionType.ADD_PARTICIPANT_TO_GROUP;
            case "\\remove_participant":
                return ActionType.REMOVE_PARTICIPANT_FROM_GROUP;
            case "\\create_o2o":
                return ActionType.CREATE_O2O;
            case "\\exit":
                return ActionType.EXIT;
            default:
                return ActionType.NONE;

        }
    }
}
