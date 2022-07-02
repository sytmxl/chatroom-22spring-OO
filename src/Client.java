import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private JTextArea jTextArea;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    int port=0;
    boolean receive = false;
    JToolBar toolBar;
    JToolBar toolBar1;
    public void init() throws IOException {
        setSize(400,800);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Client");
        setLayout(null);
        setBackground(Color.BLACK);
        setForeground(Color.white);

        Font font1 = new Font(null,Font.PLAIN,16);
        jTextArea = new JTextArea();
        jTextArea.setBounds(0,30,400,670);
        jTextArea.setFont(font1);

        add(jTextArea);

        JTextField jTextField = new JTextField();
        jTextField.setFont(font1);
        jTextField.setBounds(25,700,350,50);
        add(jTextField);

        Action a1 = new AbstractAction("所有客户端") {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                try {
                    dataOutputStream.writeUTF("/all");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        Action a2 = new AbstractAction("目标端口") {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                try {
                    dataOutputStream.writeUTF("/to");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        Action a3 = new AbstractAction("我的端口") {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                set(String.valueOf("你的端口："+port));
            }
        };
        Action a4 = new AbstractAction("清空") {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                jTextArea.setText("");
            }
        };
        Action a5 = new AbstractAction("退出") {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                set("您已下线");
                System.exit(0);
            }
        };
        toolBar = new JToolBar("func");
        toolBar.add(new JButton(a1));
        toolBar.add(new JButton(a2));
        toolBar.add(new JButton(a3));
        toolBar.add(new JButton(a4));
        toolBar.add(new JButton(a5));
        toolBar.setSize(400, 30);
        add(toolBar, BorderLayout.SOUTH);

        jTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = jTextField.getText();
                jTextField.setText("");

                if (text.equals("/me")) {
                    set(String.valueOf("你的端口："+port));
                }
                else if (text.equals("/quit")) {
                    set("您已下线");
                    System.exit(0);
                }
                else {
                    try {
                        send(text);
                        if (!text.startsWith("/")) {
                            set("发送:" + text);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        setVisible(true);

        try {
            Socket socket = new Socket("127.0.0.1", Server.serverport);
            InputStream inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            String info = dataInputStream.readUTF();//获得port
            port = Integer.parseInt(info);
            dataOutputStream.writeUTF("已上线");
        } catch (IOException e) {
            set("服务端未运行");
        }

        recive();
    }

    public void recive(){
        String info;
        try{
            while (true) {
                info = dataInputStream.readUTF();
                String[] infos = info.split(":");
                if (infos.length == 1) {//输入为空
                    continue;
                }
                if (infos[0].equals("0")) {
                    set(infos[1]);
                } else if (infos[1].equals("/quit")) {
                    set(infos[0] + "已下线");
                }
                else {
                    set(info);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void send(String info) throws IOException {
        dataOutputStream.writeUTF(info);
    }

    public static void main(String[] args) throws IOException {
        Client t = new Client();
        t.init();
    }

    void set(String info) {
        jTextArea.append('\n' + info);
    }
}
