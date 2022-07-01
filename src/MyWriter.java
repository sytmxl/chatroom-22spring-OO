import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyWriter extends Thread {
    private DataOutputStream dataOutputStream;
    int port;
    public MyWriter(DataOutputStream dataOutputStream, int port) {
        this.dataOutputStream = dataOutputStream;
        this.port = port;
    }

    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String info;
        try {
            while (true) {
                info = bufferedReader.readLine();
                if (info.equals("/all")) {
                    System.out.printf("共%d个客户端\n", Server.sockets.size());
                    for (Integer integer : Server.sockets.keySet()) {
                        System.out.println(integer);
                    }
                }
                else if (info.equals("/me")) {
                    System.out.println(port);
                }
                else if (info.startsWith("/to")) {
                    int port = Integer.parseInt(info.split(" ")[1]);
                    if (Server.sockets.keySet().contains(port)) {
                        Socket socket = Server.sockets.get(port);
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        //dataOutputStream.writeUTF(port + ":" + info);
                        System.out.println("连接到"+port);
                    }
                    else {
                        System.out.println("用户不存在");
                    }
                }
                else {
                    dataOutputStream.writeUTF(port + ":" + info);
                }
                if (info.equals("/quit")) {
                    dataOutputStream.writeUTF(port + ":" + info);
                    System.out.println("您已下线");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
