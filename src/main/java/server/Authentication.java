package server;

import lombok.Data;

@Data
public class Authentication {

    private String username;
    private String sessionKey;
}
