package iofile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JProgressBar;

public class ClientFile extends Thread {

    private static String ip;
    private static int port;
    private String filepath;
    private long size;
    private JProgressBar jprogressbar;
    public ClientFile(String ip, int port, String filepath, JProgressBar jprogressbar) {
        this.ip = ip;
        this.port = port;
        this.filepath = filepath;
        this.jprogressbar = jprogressbar;
    }
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            File file = new File(filepath);
            // 第一次传输文件名字and文件的大小
            String str1 = file.getName() + "\t" + file.length();
            output.write(str1.getBytes());
            output.flush();
            byte[] b = new byte[100];
            int len = input.read(b);
            String s = new String(b, 0, len);
            // 如果服务器传输过来的是ok那么就开始传输字节
            if (s.equalsIgnoreCase("ok")) {
                long size = 0;
                jprogressbar.setMaximum((int) (file.length() / 10000));// 设置进度条最大值
                FileInputStream fin = new FileInputStream(file);
                byte[] b1 = new byte[1024 * 1024 * 2];
                while (fin.available() != 0) {
                    len = fin.read(b1);
                    output.write(b1, 0, len);
                    output.flush();
                    size += len;
                    jprogressbar.setValue((int) (size / 10000));
                }
                if (fin.available() == 0) {
                    javax.swing.JOptionPane.showMessageDialog(null, "传输完毕！即将推出......");
                    try {
                        Thread.sleep(1500);
                        System.exit(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                output.close();
                fin.close();
                socket.close();
            } else {
                // 传输的不是ok那么就弹出个信息框对方拒绝
                javax.swing.JOptionPane.showMessageDialog(null, "对方拒绝接收此数据！");
            }
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "IOException");
        }
    }

}
