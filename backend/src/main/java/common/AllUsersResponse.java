package common;

import common.domain.Response;
import lombok.Data;
import server.Session;

import java.util.ArrayList;
import java.util.Map;

@Data
public class AllUsersResponse extends Response {

    ArrayList<String> users;

    public AllUsersResponse(Session sender, boolean actionSucceed, ErrorCode errorCode, ArrayList<String> users) {
        super(sender, actionSucceed, errorCode);
        this.users = users;
    }
}
