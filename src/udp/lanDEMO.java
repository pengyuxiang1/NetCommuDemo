package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * udp广播代码，需要另外开启一个接收端口是40008的进程
 */
class lanSend {
    /**
     *   * @param args
     *   * @throws Exception
     *  
     */

    //广播地址
    private static final String BROADCAST_IP = "230.0.0.1";//广播IP
    private static final int getPort = 40005; // 不同的port对应不同的socket发送端和接收端


    MulticastSocket broadSocket;//用于接收广播信息
    InetAddress broadAddress;//广播地址
    DatagramSocket sender;//数据流套接字 相当于码头，用于发送信息

    public lanSend() {
        try {
            //初始化
            broadSocket = new MulticastSocket(getPort);//绑定端口
            broadAddress = InetAddress.getByName(BROADCAST_IP); //获取了所有的广播地址了
            sender = new DatagramSocket(); //udp
        } catch (Exception e) {
// TODO: handle exception
            System.out.println("*****lanSend初始化失败*****" + e.toString());
        }
    }

    void join() {
        try {
            broadSocket.joinGroup(broadAddress); //加入到组播地址，这样就能接收到组播信息   加入了地址为broadAddress的Group，此时可以接收两种数据包：本地ip加上40005和组播ip加上40005
            new Thread(new GetPacket()).start(); //新建一个线程，用于循环侦听端口信息
        } catch (Exception e) {
// TODO: handle exception
            System.out.println("*****加入组播失败*****");
        }

    }


    //广播发送查找在线用户
    void sendGetUserMsg() {
        byte[] b = new byte[1024];
        DatagramPacket packet; //数据包，相当于集装箱，封装信息
        try {
            b = ("find@" + lanDEMO.msg).getBytes();
            packet = new DatagramPacket(b, b.length, broadAddress, 40008); //广播信息到指定端口：255.0.0.1：40005
            sender.send(packet);
            System.out.println("*****已发送请求*****");
        } catch (Exception e) {
            System.out.println("*****查找出错*****");
        }
    }


    //当局域网内的在线机子收到广播信息时响应并向发送广播的ip地址主机发送返还信息，达到交换信息的目的
    void returnUserMsg(String ip) {
        byte[] b = new byte[1024];
        DatagramPacket packet;
        try {
            b = ("retn@" + lanDEMO.msg).getBytes();
            packet = new DatagramPacket(b, b.length, InetAddress.getByName(ip), 40008);//来源地址获取：返回的信息可不能是组播了
            sender.send(packet);
            System.out.print("发送信息成功！");
        } catch (Exception e) {
// TODO: handle exception
            System.out.println("*****发送返还信息失败*****");
        }
    }


    //当局域网某机子下线是需要广播发送下线通知
    void offLine() {
        byte[] b = new byte[1024];
        DatagramPacket packet;
        try {
            b = ("offl@" + lanDEMO.msg).getBytes();
            packet = new DatagramPacket(b, b.length, broadAddress, 40008);
            sender.send(packet);
            System.out.println("*****已离线*****");
        } catch (Exception e) {
// TODO: handle exception
            System.out.println("*****离线异常*****");
        }
    }

    class GetPacket implements Runnable {//新建的线程，用于侦听

        public void run() {
            DatagramPacket inPacket;

            String[] message;
            while (true) {
                try {
                    inPacket = new DatagramPacket(new byte[1024], 1024);
                    broadSocket.receive(inPacket); //接收广播信息并将信息封装到inPacket中
                    message = new String(inPacket.getData(), 0, inPacket.getLength()).split("@"); //获取信息，并切割头部，判断是何种信息（find--上线，retn--回答，offl--下线）

                    //if (message[1].equals(lanDEMO.ip)) continue;//忽略自身

                    if (message[0].equals("find")) {//如果是请求信息
                        System.out.println("新上线主机：" + " ip：" + message[1] + " 主机：" + message[2]);
                        returnUserMsg(message[1]);//message1是剩余部分
                        //记录此ip和注册名字，这是后来不发请求的时候
                    } else if (message[0].equals("retn")) {//如果是返回信息
                        System.out.println("找到新主机：" + " ip：" + message[1] + " 主机：" + message[2]);
                        //记录此ip和注册名字，这个情况是第一次的时候
                        //加入到本地地址表
                    } else if (message[0].equals("offl")) { //如果是离线信息
                        System.out.println("主机下线：" + " ip：" + message[1] + " 主机：" + message[2]);
                        //删除地址表该项
                    }

                } catch (Exception e) {
// TODO: handle exception
                    System.out.println("线程出错 " + e);
                }
            }
        }
    }
}


public class lanDEMO {

    //全局变量

    public static String msg;

    public static String ip;
    public static String hostName;

    public static void main(String[] args) {   //程序入口点
        lanSend lSend;
        while(true){
            try {
                InetAddress addr = InetAddress.getLocalHost();
                ip = addr.getHostAddress().toString();
                hostName = addr.getHostName().toString();
                msg = ip + "@" + hostName;
                lSend = new lanSend();
                lSend.join(); //加入组播，并创建线程侦听
                lSend.sendGetUserMsg(); //广播信息，寻找上线主机交换信息
                Thread.sleep(30000); //程序睡眠3秒
                lSend.offLine(); //广播下线通知
            } catch (Exception e) {
                System.out.println("*****获取本地用户信息出错*****");
            }finally {
//                lSend.offLine(); //广播下线通知
            }
        }

    }

}
