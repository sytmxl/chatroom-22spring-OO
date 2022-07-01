import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class Server {
    private static ServerSocket serverSocket=null;
    static public HashMap<Integer, Socket> sockets = new HashMap<>();
    public static void main (String args[]) {
        try {
            serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, 8888);

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("8888:"+String.valueOf(socket.getPort()));//传输port给client

                Server.sockets.put(socket.getPort(), socket);
                serverThread.start();
            }
        }
        catch (SocketException e) {
            System.out.println("网络连接异常");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
