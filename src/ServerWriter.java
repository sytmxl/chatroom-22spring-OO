import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerWriter extends Thread {
    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String info;
        try {
            while (true) {
                info = bufferedReader.readLine();
                int port;
                if (info.equals("/all")) {
                    System.out.printf("共%d个客户端\n", Server.sockets.size());
                    for (Integer integer : Server.sockets.keySet()) {
                        System.out.println(integer);
                    }
                } else if (info.equals("/me")) {
                    System.out.println(Server.serverport);
                }
                else if (info.startsWith("/toall")) {
                    info = info.split(" ")[1];
                    for (Socket socket : Server.sockets.values()) {
                        new DataOutputStream(socket.getOutputStream()).writeUTF(Server.serverport + ":" + info);
                    }
                }
                else if (info.startsWith("/to")) {
                    if (info.equals("/to")) {
                        System.out.println(Server.toport);
                        continue;
                    }
                    port = Integer.parseInt(info.split(" ")[1]);
                    if (Server.sockets.keySet().contains(port)) {
                        Server.toport = port;
                        System.out.println("连接到" + port);
                    } else {
                        System.out.println("用户不存在");
                    }
                }
                 else {
                    Server.send(info);
                }
                if (info.equals("/quit")) {
                    System.out.println("您已下线");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
