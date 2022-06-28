import java.io.*;
import java.net.Socket;

public class Client {
    public static void main (String[] args) throws IOException {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            new MyServerReader(dataInputStream).start();
            new MyServerWriter(dataOutputStream).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
