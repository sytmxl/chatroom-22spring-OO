import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyServerWriter extends Thread {
    private DataOutputStream dataOutputStream;

    public MyServerWriter(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String info;
        try {
            while (true) {
                info = bufferedReader.readLine();
                dataOutputStream.writeUTF(info);
                if (info.equals("bye")) {
                    System.out.println("我下线了");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
