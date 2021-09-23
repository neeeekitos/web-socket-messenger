package common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Authentication implements Serializable {

    private String username;
    private String sessionKey;
}
