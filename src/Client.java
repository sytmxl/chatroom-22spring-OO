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

            String info = dataInputStream.readUTF();
            int port = Integer.parseInt(info.split(":")[1]);

            new ClientReader(dataInputStream, port).start();
            new ClientWriter(dataOutputStream, port).start();
        } catch (IOException e) {
            System.out.println("服务端未运行");
        }
    }
}
