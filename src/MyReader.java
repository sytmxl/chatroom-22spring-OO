import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class MyReader extends Thread {
    private DataInputStream dataInputStream;
    int port;

    public MyReader(DataInputStream dataInputStream, int port) {
        this.dataInputStream = dataInputStream;
        this.port = port;
    }

    @Override
    public void run() {
        String info;
        try{
            while (true) {
                info = dataInputStream.readUTF();
                String [] infos = info.split(":");
                if (infos.length == 1) {//输入为空
                    continue;
                }
                System.out.println(info);
                if (infos[1].equals("/quit")) {
                    System.out.println(infos[0]+"已下线");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
