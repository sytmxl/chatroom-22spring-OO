import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientWriter extends Thread {
    private DataOutputStream dataOutputStream;
    int port;
    public ClientWriter(DataOutputStream dataOutputStream, int port) {
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
                if (info.equals("/me")) {
                    System.out.println(port);
                }
                else if (info.equals("/quit")) {
                    System.out.println("您已下线");
                    System.exit(0);
                }
                else {
                    send(info);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void send(String info) throws IOException {
        dataOutputStream.writeUTF(info);
    }
}
