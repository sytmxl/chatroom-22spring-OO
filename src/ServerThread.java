import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    int port;
    int toport;

    public ServerThread(Socket socket, int port, int toport) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        InputStream inputStream = socket.getInputStream();
        dataInputStream = new DataInputStream(inputStream);
        this.port= port;
        this.toport=toport;
    }

    @Override
    public void run() {
        new ServerReader(dataInputStream, toport).start();
    }
}
