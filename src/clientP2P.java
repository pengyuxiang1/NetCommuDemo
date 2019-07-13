import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 */
public class clientP2P extends Thread implements ActionListener{
    //是否停止
    public static int STOP=0;

    //在线用户列表  对应服务器的users
    public static Map<String,SocketAddress> userMap=new HashMap();

    private DatagramSocket client;

    private JFrame frame;
    //聊天信息
    private JTextArea info;
    //在线用户
    private JTextArea onlineUser;
    private JTextField msgText;

    private JButton sendButton;

    public clientP2P(DatagramSocket client)throws Exception
    {

        this.client=client;
        this.frame=new JFrame("P2P聊天");
        frame.setSize(800, 400);

        sendButton=new JButton("发送");
        JScrollBar scroll=new JScrollBar();
        this.info=new JTextArea(10,30);
        //激活自动换行功能
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        scroll.add(info);

        onlineUser=new JTextArea(10,30);
        onlineUser.setLineWrap(true);
        onlineUser.setWrapStyleWord(true);
        onlineUser.setEditable(false);


        JPanel infopanel=new JPanel();
        infopanel.add(info,BorderLayout.WEST);
        JPanel infopanel1=new JPanel();
        JLabel label=new JLabel("在线用户");
        infopanel1.add(label, BorderLayout.NORTH);
        infopanel1.add(onlineUser, BorderLayout.SOUTH);
        infopanel.add(infopanel1,BorderLayout.EAST);

        JPanel panel=new JPanel();

        msgText=new JTextField(30);

        panel.add(msgText);
        panel.add(sendButton);
        frame.add(infopanel,BorderLayout.NORTH);
        frame.add(panel,BorderLayout.SOUTH);
        frame.setVisible(true);

        sendButton.addActionListener(this);

        frame.addWindowListener(new   WindowAdapter(){
            public   void   windowClosing(WindowEvent   e){
                System.exit(0);
            }
        });


    }

    /**
     * 给其他在线用户发送心跳 保持session有效。不是有了服务器管理地址吗，为什么还要自己也去联系其他的在线用户？
     */
    private void sendSkip()
    {
        //这种方式实现两种线程
        new Thread(){
            public void run()
            {
                try
                {
                    String msg="skip";
                    while(true)
                    {
                        if(STOP==1)
                            break;
                        if(userMap.size()>0)
                        {
                            for (Entry<String, SocketAddress> entry : userMap.entrySet()) {
                                DatagramPacket data=new DatagramPacket(msg.getBytes(),msg.getBytes().length,entry.getValue());
                                client.send(data);
                            }
                        }
                        //每10s发送一次心跳，
                        Thread.sleep(10*1000);
                    }
                }catch(Exception e){}

            }
        }.start();
    }

    //主要任务是接收数据
    //可以是其他用户发来的信息，也可以是服务器发来的在线用户数据
    public void run()
    {
        try
        {

            String msg;
            DatagramPacket data;

            //执行心跳
            sendSkip();

            while(true)
            {
                if(STOP==1)
                    break;
                //接收区
                byte[] buf=new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                client.receive(packet);

                //消息处理
                msg=new String(packet.getData(),0,packet.getLength());
                if(msg.length()>0)
                {
                    //如果是服务器发来的更新地址，则广播存在，并且更新在线（？？）用户列表
                    if(msg.indexOf("server:")>-1)
                    {
                        //服务器数据 格式server:ID#IP：PORT，。。
                        String userdata=msg.substring(msg.indexOf(":")+1,msg.length());//userdata是将“server：”去掉后的字符串
                        String[] user=userdata.split(",");//每一组id+ip+port组成一个user
                        for(String u:user)
                        {
                            if(u!=null&&u.length()>0)
                            {
                                /**                  所有地址的分割处理                        **/
                                String[] udata=u.split("#");//获得id和地址相间的数组
                                String ip=udata[1].split(":")[0];//将ip+port分割并取出ip
                                int port=Integer.parseInt(udata[1].split(":")[1]);//将ip+port分割并取出地址
                                //将ip字符串中多出的分割标记ip【0】=# 去除
                                ip=ip.substring(1,ip.length());
                                //生成地址对象（不是字符串形式）
                                SocketAddress adds=new InetSocketAddress(ip,port);
                                //id和地址又一次重组成为了一个map。
                                userMap.put(udata[0], adds);


                                //给对方打洞 发送空白报文
                                data=new DatagramPacket(new byte[0],0,adds);
                                client.send(data);
                            }

                        }
                        //更新在线用户列表
                        this.onlineUser.setText("");
                        //名字似乎很丑啊，是id+地址
                        for (Map.Entry<String, SocketAddress> entry : userMap.entrySet()) {
                            this.onlineUser.append("用户"+entry.getKey()+"("+entry.getValue()+")\n");
                        }
                    }
                    else if(msg.indexOf("skip")>-1);
                    else
                    {
                        //普通消息
                        this.info.append(packet.getAddress().toString()+packet.getPort()+" 说："+msg);
                        this.info.append("\n");
                    }
                }
            }
        }
        catch(Exception e){}
    }

    public static void main(String args[])throws Exception
    {

        String serverIP="192.168.1.188";
        int port=6636;

        //构造一个目标地址
        SocketAddress target = new InetSocketAddress(serverIP, port);

        DatagramSocket client = new DatagramSocket();
        String msg="向服务器报告！";
        byte[] buf=msg.getBytes();
        //向服务器发送上线数据
        DatagramPacket packet=new DatagramPacket(buf,buf.length,target);
        client.send(packet);
        new clientP2P(client).start();

    }


    //按钮事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==this.sendButton)
        {
            try{
                String msg=this.msgText.getText();
                if(msg.length()>0)
                {
                    this.info.append("我说："+msg);
                    this.info.append("\n");
                    //发言是广播？？？
                    for (Map.Entry<String, SocketAddress> entry : userMap.entrySet()) {
                        DatagramPacket data=new DatagramPacket(msg.getBytes(),msg.getBytes().length,entry.getValue());
                        client.send(data);
                    }

                    this.msgText.setText("");
                }
            }
            catch(Exception ee){}
        }

    }
}