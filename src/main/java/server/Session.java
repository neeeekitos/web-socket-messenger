package server;

import lombok.Data;

@Data
public class Session {

    private String username;
    private String secureSessionKey;

}
