package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.Session;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Data
@AllArgsConstructor
public class Connection {

    private Socket socket;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private Session session;

    private boolean isAuthenticated;
}
