import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * TCP实现的Socket通信
 */

public class client2 extends JFrame implements ActionListener {

    //ui
    JTextArea jta = null;
    JTextField jtf = null;
    JButton jb = null;
    JPanel jpl= null;
    JScrollPane jsp = null;

    //把信息发给客户端的对象
    PrintWriter pw =null;


    public static void main(String [] args){
        client2 mc = new client2();//新建ui和
    }

    public client2(){
        //新建ui并且加监听
        jta = new JTextArea();
        jtf = new JTextField(20);
        jb= new JButton("发送");
        jb.addActionListener(this);//this表示这个类
        jpl = new JPanel();
        jsp = new JScrollPane(jta);
        jpl.add(jtf);
        jpl.add(jb);
        //添加ui标签到这个类中
        this.add(jsp,"Center");
        this.add(jpl,"South");
        this.setTitle("客户端");
        this.setSize(400,300);
        this.setVisible(true);

        try {
            //socket新建三部曲（连接，输入输出）
            Socket s = new Socket("127.0.0.1",9988);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream(),true);

            //监听端口数据
            while(true){
                String info = br.readLine();
                if(info!=null){
                    jta.append("服务端："+info+"\r\n");
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub
//如果用户按下发送信息按钮
        if(e.getSource()==jb){
//把服务器在框里写内容发送给客户端
            String info = jtf.getText();
            jta.append("客户端："+info+"\r\n");
            pw.println(info);//发送
            jtf.setText("");//清空输入框
        }
    }

}