import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerReader extends Thread {
    private DataInputStream dataInputStream;
    int port;//对接的client port

    public ServerReader(DataInputStream dataInputStream, int port) {
        this.dataInputStream = dataInputStream;
        this.port = port;
    }

    @Override
    public void run() {
        String info;
        String [] infos;
        try{
            while (true) {
                int to = Server.tos.get(port);
                info = dataInputStream.readUTF();
                if (info.equals("")) {//输入为空
                    continue;
                }
                if (info.startsWith("/to")) {
                    infos = info.split(" ");
                    if (infos.length == 1) {
                        send(String.valueOf(to), 0);
                    }
                    else {
                        int newto = Integer.valueOf(infos[1]);
                        if (Server.sockets.keySet().contains(newto)) {
                            Server.tos.put(port, newto);
                            send("连接到" + infos[1], 0);
                        }
                        else {
                            send("用户不存在", 0);
                        }
                    }
                }
                else if (info.startsWith("/all")) {
                    send("共"+Server.sockets.size()+"个客户端", 0);
                    for (Integer integer : Server.sockets.keySet()) {
                        send(String.valueOf(integer), 0);
                    }
                }
                else if (info.startsWith("/quit")) {
                    System.out.println(port+"已下线");
                }
                else if (to != 8888){
                    boolean ret = Server.send(to, info, port);
                    if (!ret) {
                        send("发送失败", 0);
                    }
                }
                else {
                    System.out.println(port + ":" + info);
                }
            }
        } catch (IOException e) {
            System.out.println(port+"已下线");
        }
    }
    boolean send(String info) throws IOException {//发送给自己处理的client
        if (Server.sockets.containsKey(port)) {
            Socket socket = Server.sockets.get(port);
            new DataOutputStream(socket.getOutputStream()).writeUTF(info);
            return true;
        }
        else {
            return false;
        }
    }
    boolean send(String info, int prefix) throws IOException {//发送给自己处理的client
        if (Server.sockets.containsKey(port)) {
            Socket socket = Server.sockets.get(port);
            new DataOutputStream(socket.getOutputStream()).writeUTF(prefix+":"+info);
            return true;
        }
        else {
            return false;
        }
    }
}
