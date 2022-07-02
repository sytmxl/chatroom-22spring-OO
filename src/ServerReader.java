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
                info = dataInputStream.readUTF();
                int to = Server.tos.get(port);
                if (info.equals("")) {//输入为空
                    continue;
                }
                if (info.startsWith("/to")) {
                    infos = info.split(" ");
                    if (infos.length == 1) {
                        send("当前连接到："+String.valueOf(to), 0);
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
                else if (to != 8888){
                    boolean ret = Server.send(to, info, port);
                    if (!ret) {
                        send("发送失败,对方未在线", 0);
                    }
                }
                else {
                    System.out.println(port + ":" + info);
                }
            }
        } catch (IOException e) {
            for (Socket socket : Server.sockets.values()) {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("0:"+port + "已下线");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println(port + "已下线");
            Server.sockets.remove(port);
            Server.tos.remove(port);
            for (Integer from : Server.tos.keySet()) {
                if (Server.tos.get(from) == port) {
                    if (Server.tos.size() == 1) {
                        Server.tos.put(from, 8888);
                    }
                    else {
                        int to = (int) Server.tos.keySet().toArray()[0];
                        if (to == from) {
                            to = (int) Server.tos.keySet().toArray()[1];
                        }
                        Server.tos.put(from, to);
                    }
                    try {
                        Server.send(from, "自动连接到"+ Server.tos.get(from), 0);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            relink();
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
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(prefix+":"+info);
            return true;
        }
        else {
            return false;
        }
    }

    void relink() {
        if (!Server.tos.containsKey(Server.toport)) {
            if (Server.tos.size() == 0) {
                Server.toport = 0;
            }
            else {
                Server.toport = (int) Server.tos.keySet().toArray()[0];
                System.out.println("自动连接到"+ Server.toport);
            }
        }
    }
}
