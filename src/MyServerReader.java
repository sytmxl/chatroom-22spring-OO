import java.io.DataInputStream;
import java.io.IOException;

public class MyServerReader extends Thread {
    private DataInputStream dataInputStream;

    public MyServerReader(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    @Override
    public void run() {
        String info;
        try{
            while (true) {
                info = dataInputStream.readUTF();
                System.out.println("对方说："+info);
                if (info.equals("bye")) {
                    System.out.println("对方下线");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
