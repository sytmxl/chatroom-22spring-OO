import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ServerThread extends Thread {
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    int port;


    public ServerThread(Socket socket, int port) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        InputStream inputStream = socket.getInputStream();
        dataInputStream = new DataInputStream(inputStream);
        this.port= port;
    }

    @Override
    public void run() {
        new MyReader(dataInputStream, port).start();
        new MyWriter(dataOutputStream, port).start();
    }
}
