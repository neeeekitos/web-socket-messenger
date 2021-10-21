package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Connection {

    private Socket socket;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private Session session;

    private boolean isAuthenticated;

    public void close()
    {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            isAuthenticated = false;
        }
    }
}
