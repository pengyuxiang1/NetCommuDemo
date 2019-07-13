package udp;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * 文件传输代码udp版本
 */
public class Client extends JFrame {
    // 加入属性
    private JPanel panel = new JPanel();
    private JButton button_send = new JButton("文件发送");
    private JButton button_receive = new JButton("另存为");
    private JTextArea ta = new JTextArea();
    private JScrollPane sp = new JScrollPane(ta);
    private JTextArea ta_send = new JTextArea();
    private JScrollPane sp_send = new JScrollPane(ta_send);
    private JLabel label_fileState = new JLabel("文件状态", JLabel.CENTER);
    private JLabel label_feedback = new JLabel("反馈", JLabel.CENTER);
    //
    private InetAddress ip = null;
    private int otherport;
    private int myport;
    DatagramSocket socket;// 接收文件来显提示
    DatagramSocket socket1;// 接收文件信息
    DatagramSocket socket2;// 接收平时的聊天信息
    //
    String filename = null;
    byte buffer[] = new byte[1024];
    int fileLen = 0;
    int numofBlock = 0;
    int lastSize = 0;
    //
    String str_name;

    //
    public Client(String str_name, String str_ip, int otherport, int myport) {
        super(str_name);
        this.str_name = str_name;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(600, 250, 300, 400);
        // 加入功能代码
        this.setLayout(new GridLayout(5, 1, 7, 7));
        ta.setLineWrap(true);// 换行
        this.add(sp);
        ta_send.setLineWrap(true);// 换行
        this.add(sp_send);
        button_send.setFont(new Font("楷体", 1, 20));
        button_receive.setFont(new Font("楷体", 1, 20));
        panel.add(button_send);
        panel.add(button_receive);
        this.add(panel);
        this.add(label_fileState);
        this.add(label_feedback);
        //
        this.setVisible(true);
        //
        this.otherport = otherport;
        this.myport = myport;
        //
        button_send.addActionListener(new ActionListener() {// 发送文件
            public void actionPerformed(ActionEvent e) {
                // 选择要发送的文件
                JFileChooser filechooser = new JFileChooser();
                int result = filechooser.showOpenDialog(Client.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = filechooser.getSelectedFile();
                        try {
                            // 将文件名称发送过去
                            String str_filename = file.getName();
                            String str_tip = "有文件，请处理：" + str_filename;
                            byte[] fileNameBuf = str_tip.getBytes();
                            DatagramSocket socket = new DatagramSocket();
                            DatagramPacket packet = new DatagramPacket(
                                    fileNameBuf, fileNameBuf.length,
                                    ip, Client.this.otherport);
                            socket.send(packet);
                            socket.close();
                            //
                            FileInputStream fis = new FileInputStream(file);// 从文件里取出写入内存
                            // 将文件长度发送过去
                            int fileLen = fis.available();
                            String str_len = "" + fileLen;
                            byte[] fileLenBuf = str_len.getBytes();
                            socket = new DatagramSocket();
                            packet = new DatagramPacket(fileLenBuf,
                                    fileLenBuf.length, ip,
                                    Client.this.otherport+1);
                            socket.send(packet);
                            socket.close();

                            // 发送文件主体
                            byte[] buf = new byte[1024];
                            int numofBlock = fileLen / buf.length;// 循环次数（将该文件分成了多少块）
                            int lastSize = fileLen % buf.length;// 最后一点点零头的字节数
                            socket = new DatagramSocket();
                            for (int i = 0; i < numofBlock; i++) {
                                fis.read(buf, 0, buf.length);// 写入内存
                                packet = new DatagramPacket(buf,
                                        buf.length, ip,
                                        Client.this.otherport+1);
                                socket.send(packet);
                                Thread.sleep(1); // 简单的防止丢包现象
                            }
                            // 发送最后一点点零头
                            fis.read(buf, 0, lastSize);
                            packet = new DatagramPacket(buf,
                                    buf.length, ip,
                                    Client.this.otherport+1);
                            socket.send(packet);
                            Thread.sleep(1); // 简单的防止丢包现象
                            //
                            fis.close();
                            socket.close();
                            //
                            label_fileState.setText("文件传输完成！ ");
                                    ta.append("");
                            //
                        } catch (Exception ev) {
                            System.out.println(ev);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Client.this,
                                "打开文档出错。");
                    }
                }
            }
        });

        button_receive.addActionListener(new ActionListener() {// 接收文件
            public void actionPerformed(ActionEvent e) {
                // 选择要接收的文件
                JFileChooser filechooser = new JFileChooser();
                int result = filechooser.showSaveDialog(Client.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file2 = filechooser.getSelectedFile();
                        try {
                            File file1 = new File("D:\\TT\\" + filename);
                            saveAs(file1, file2);
                            //
                            label_fileState.setText("文件接收完成！");
                            ta.append("文件已处理！！\n");
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Client.this,
                                "打开保存出错！ ");
                    }
                }
            }
        });
        //接受消息
        ta_send.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String str_chat = Client.this.str_name + " 说： "
                        + ta_send.getText();
                byte buf[] = str_chat.getBytes();
                if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        DatagramPacket packet = new DatagramPacket(buf,
                                buf.length, ip, Client.this.otherport+2);
                        socket.send(packet);
                        ta.append("我说： "+ta_send.getText()+"\n");
                        ta_send.setText("");
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        });
        //
        try {
            ip = InetAddress.getByName(str_ip);
            socket = new DatagramSocket(this.myport);
            socket1 = new DatagramSocket(this.myport + 1);
            socket2 = new DatagramSocket(this.myport + 2);
            Timer timer = new Timer();//定时器，刷新接收消息
            timer.schedule(new MyTimerTask_receive(),0, 100);
            while (socket!=null) {
                try {
                    //
                    byte filetipBuf[] = new byte[256];// 防止文件名称字过长（此处最长256个字符）
                    DatagramPacket packet_tip = new DatagramPacket(filetipBuf,
                            0, filetipBuf.length);
                    socket.receive(packet_tip);
                    String str_filetip = new String(packet_tip.getData(), 0,
                            packet_tip.getLength());
                    filename = str_filetip.substring(8);
                    ta.append(str_filetip + "\n");
                    // 接收文件长度（字节数）
                    byte[] fileLenBuf = new byte[12];// 能够传输1T的文件
                    DatagramPacket packet_len = new DatagramPacket(fileLenBuf,
                            fileLenBuf.length);
                    socket1.receive(packet_len);
                    String str_fileLen = new String(fileLenBuf, 0,
                            packet_len.getLength());
                    fileLen = Integer.parseInt(str_fileLen);
                    ta.append("文件大小： " + fileLen + "字节, " + (fileLen / 1024)
                            + "kb, " + (fileLen / 1024 / 1024) + "Mb\n");
                    //
                    DatagramPacket packet_file = new DatagramPacket(buffer, 0,
                            buffer.length);
                    numofBlock = fileLen / buffer.length;// 循环次数（将该文件分成了多少块）
                    lastSize = fileLen % buffer.length;// 最后一点点零头的字节数
                    File file = new File("D:\\TT\\" + filename);
                    FileOutputStream fos = new FileOutputStream(file);// 从内存取出存入文件
                    for (int i = 0; i < numofBlock; i++) {
                        packet_file = new DatagramPacket(buffer, 0,
                                buffer.length);
                        socket1.receive(packet_file);// 通过套接字接收数据
                        fos.write(buffer, 0, 1024);// 写入文件
                    }
                    // 接收最后一点点零头
                    packet_file = new DatagramPacket(buffer, 0, lastSize);
                    socket1.receive(packet_file);// 通过套接字接收数据
                    fos.write(buffer, 0, lastSize);// 写入文件
                    fos.close();
                    // 反馈包
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //另存为
    public void saveAs(File file1, File file2) {// 把file1另存为file2,并删掉file1
        try {
            FileInputStream fis = new FileInputStream(file1);
            FileOutputStream fos = new FileOutputStream(file2);
            byte buf[] = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
            file1.delete();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    class MyTimerTask_receive extends TimerTask {
        public void run() {
            try{
                byte chatBuf[] = new byte[512];
                DatagramPacket packet_chat = new DatagramPacket(chatBuf, 0,
                        chatBuf.length);
                socket2.receive(packet_chat);
                String str_chat = new String(packet_chat.getData(), 0,
                        packet_chat.getLength());
                ta.append(str_chat + "\n");
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }

    // ////////////////////////////////////////////////////
    public static void main(String args[]) {
        new Client("Mary", "127.0.0.2", 6000, 10000);
    }
}
