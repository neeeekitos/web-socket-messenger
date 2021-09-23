package common;

public class Action extends BatchEntity {



    private enum ActionType {
        CREATE_GROUP,
        DELETE_GROUP,
        ADD_PARTICIPANT_TO_GROUP,
        REMOVE_PARTICIPANT_FROM_GROUP,
        CREATE_O2O
    }
}
