package server;

import lombok.Data;

import java.io.Serializable;

@Data
public class Session implements Serializable {

    private String username;
    private String secureSessionKey;

}
