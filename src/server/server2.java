package server;/*=============服务端================*/

/**
 * 服务器程序 在9999端口监听
 * 可以通过控制台输入来回应客户端
 * @author xiaoluo
 * @qq 3087438119
 * 通过socket可以接受一个jpl指针，实现了各个线程的回复最后统一到了一个窗口，之后可以用这个方法实现多个
 * 好友同时聊天，但是，值得注意的是，可以通过子孙继承法实现继承多个类，以此达到一个每个线程都有一个窗口
 * 的目的，但是，现在想来，似乎没有必要。要的就是建立一个窗口却可以对于多个人的效果，不过，如果真的是那
 * 样，估计还是要map来实现点对点了。
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP实现的Socket通信
 */

class aThread2 extends Thread{
    Socket socket;
    JTextArea jta = null;
    PrintWriter pw;
    public aThread2(Socket s ,JTextArea jta2,PrintWriter pw2){
        socket=s;
        jta=jta2;
        pw=pw2;
    }

    @Override
    public void run() {
        try {
            BufferedReader bf=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            while(true){
                String line=bf.readLine();
                jta.append("客户端:"+line+"\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


public class server2 extends JFrame implements ActionListener{
    //测试类
    int i=0;
    List<PrintWriter> list=new ArrayList<PrintWriter>();
    JButton jb2 = null;

    //ui
    private static final long serialVersionUID = 1L;
    JTextArea jta = null;
    JTextField jtf = null;
    JButton jb = null;
    JPanel jpl= null;
    JScrollPane jsp = null;
    //把信息发给客户端的对象
    PrintWriter pw =null;
    public static void main(String [] args){
        server2 ms = new server2();
    }
    public server2(){
        ////新建ui并且加监听
        jta = new JTextArea();
        jtf = new JTextField(20);
        jb= new JButton("发送");
        jb.addActionListener(this);
        jpl = new JPanel();
        jsp = new JScrollPane(jta);
        jpl.add(jtf);
        jpl.add(jb);

        jb2= new JButton("切换好友");
        jb2.addActionListener(this);
        jpl.add(jb2);

        //添加ui标签到这个类中
        this.add(jsp,"Center");
        this.add(jpl,"South");
        this.setTitle("和佳客服");
        this.setSize(400,300);
        this.setVisible(true);

        try {
            ServerSocket ss=new ServerSocket(9988);
            while(true){
                Socket s=ss.accept();
                pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
                System.out.println(pw);
                list.add(pw);
                new aThread2(s,jta,pw).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//如果用户按下发送信息按钮
        if(e.getSource()==jb){
//把服务器在框里写内容发送给客户端
            String info = jtf.getText();
            jta.append("服务端："+info+"\r\n");
            System.out.println(pw);
            pw.println(info);//发送
            jtf.setText("");//清空输入框
        }
        if(e.getSource()==jb2){
            pw=list.get(i++);
        }
    }

}