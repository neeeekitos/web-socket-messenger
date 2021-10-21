package common;

public enum ErrorCode {
    NONE("Success"),
    NO_CHAT_CREATED("You must create a chat before."),
    NO_MATCHING_CHAT("No matching chat was found."),
    PARTICIPANT_ALREADY_ADDED("Participant already added."),
    INTERNAL_ERROR("An internal error occurred on the server."),
    CAN_NOT_REMOVE_ITSELF("You can not remove yourself."),
    PARTICIPANT_NOT_EXIST_IN_CHAT("Participant does not exist in the chat"),
    ONLY_ADMIN_CAN_REMOVE_PARTICIPANT("Only admin can remove participant."),
    O2O_ALREADY_EXISTS("One2one has been already created.");

    private final String identifier;

    ErrorCode(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    public static ErrorCode getActionTypeByIdentifier(String identifier) {
        for(ErrorCode e : values()) {
            if(e.identifier.equals(identifier)) return e;
        }
        return NONE;
    }
}