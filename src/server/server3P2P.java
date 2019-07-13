package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务端
 * @author ggxin
 * 似乎暂时没有体现出users和userList的差别。
 */
public class server3P2P extends Thread{
    //存储所有的用户IP与端口
    public static List<Map> userList=new ArrayList<Map>();
    //在线用户IP
    public static Map users=new HashMap();

    public static int index=1;

    private DatagramSocket server;

    public server3P2P(DatagramSocket server)
    {
        this.server=server;
    }
    //线程负责给在线用户发送当前所有在线用户的信息
    public void run()
    {
        try
        {
            DatagramPacket sendPacket;
            StringBuffer msg;
            while(true)
            {
                //是否应该吧userList换成users？
                for(Map user:server3P2P.userList)
                {
                    //服务器数据，标记server:
                    msg=new StringBuffer("server:");
                    //第二个循环for目的：将所有的地址放到mg中
                    for(Map map:server3P2P.userList)
                    {
                        if(!map.get("id").toString().equals(user.get("id").toString()))
                        {
                            msg.append(map.get("id")+"#"+map.get("ip")+":"+map.get("port"));
                            msg.append(",");
                        }
                    }
                    //将msg中的地址，发给users这个map中的每一个地址（通过第一个循环for）
                    if(!msg.toString().equals("server:"))
                    {
                        byte[] data=msg.toString().getBytes();
                        //构造发送报文
                        sendPacket = new DatagramPacket(data, data.length, (InetAddress)user.get("ip"), (Integer)user.get("port"));
                        server.send(sendPacket);
                    }
                }
                //间隔2s通知更新一次地址
                Thread.sleep(2000);

            }
        }catch(Exception e){}
    }


    public static void main(String args[])throws Exception
    {

        int port=6636;

        //创建一个UDPsocket
        DatagramSocket server = new DatagramSocket(port);
        byte[] buf = new byte[1024];
        //接收数据的udp包
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        //开启服务,此服务主要是为了实现服务器的第一个功能：向每一个主机更新地址表
        new server3P2P(server).start();

        String msg;
        //循环接收数据
        while(true)
        {
            //等待接收数据，实现服务器的第二个功能：更新服务器的地址表
            server.receive(packet);

            msg=new String(packet.getData(),0,packet.getLength());

            if(msg!=null&&msg.equals("bye"))
                break;

            if(msg.length()>0)
            {
                //新增地址
                System.out.println("收到数据来自：("+packet.getAddress()+":"+packet.getPort()+")="+msg);
                //users没有的，又是刚来的，所以是在线的新用户。
                if(!users.containsKey(packet.getAddress()+":"+packet.getPort()))
                {
                    Map map=new HashMap();
                    map.put("id", index);
                    map.put("ip", packet.getAddress());
                    map.put("port", packet.getPort());
                    userList.add(map);

                    users.put(packet.getAddress()+":"+packet.getPort(), index);
                    index++;
                }

            }
        }
        server.close();
    }

}