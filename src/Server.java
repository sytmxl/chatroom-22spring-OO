import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    public static void main (String args[]) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket1 = serverSocket.accept();
            OutputStream outputStream = socket1.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            InputStream inputStream = socket1.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            new MyServerReader(dataInputStream).start();
            new MyServerWriter(dataOutputStream).start();
        }
        catch (SocketException e) {
            System.out.println("网络连接异常");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
