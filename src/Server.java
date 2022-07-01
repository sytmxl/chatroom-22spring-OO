import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class Server {
    private static ServerSocket serverSocket=null;
    static public HashMap<Integer, Socket> sockets = new HashMap<>();
    static public HashMap<Integer, Integer> tos = new HashMap<>();
    static public int toport=0;
    static public int serverport=8888;
    public static void main (String[] args) {
        try {
            serverSocket = new ServerSocket(serverport);
            new ServerWriter().start();
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, serverport, socket.getPort());

                if (toport==0) {
                    toport = socket.getPort();
                }

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(String.valueOf(socket.getPort()));//传输port给client

                sockets.put(socket.getPort(), socket);
                tos.put(socket.getPort(), serverport);
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

    public static boolean send(String info) throws IOException {//发送给server目前保存的目标client
        int port = toport;
        info = serverport + ":" + info;
        if (sockets.containsKey(port)) {
            Socket socket = sockets.get(port);
            new DataOutputStream(socket.getOutputStream()).writeUTF(info);
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean send(String info, int prefix) throws IOException {//发送给server目前保存的目标client
        int port = toport;
        info = prefix + info;
        if (sockets.containsKey(port)) {
            Socket socket = sockets.get(port);
            new DataOutputStream(socket.getOutputStream()).writeUTF(info);
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean send(int port, String info) throws IOException {//发送给server目前保存的目标client
        info = serverport + ":" + info;
        if (sockets.containsKey(port)) {
            Socket socket = sockets.get(port);
            new DataOutputStream(socket.getOutputStream()).writeUTF(info);
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean send(int port, String info, int prefix) throws IOException {//发送给server目前保存的目标client
        info = prefix + ":" + info;
        if (sockets.containsKey(port)) {
            Socket socket = sockets.get(port);
            new DataOutputStream(socket.getOutputStream()).writeUTF(info);
            return true;
        }
        else {
            return false;
        }
    }
}
