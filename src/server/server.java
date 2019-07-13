package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TCP实现的Socket通信
 */

class aThread extends Thread{
    private Socket socket;
    public aThread(String s){
        super(s);
    }

    public aThread(Socket s){
        socket=s;
    }

    public void run(){
        try {
            //BufferedReader bufReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader bufReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            //String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            String time = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
            while (true) {
                String Line=bufReader1.readLine();
                if (Line.equals("over")) {
                    break;
                }
                System.out.println("接收到了客戶端信息：" + Line);
//向客户端发送接收到的数据
                out.println("客戶，我在" + time+ "已經收到了你的信息: " + Line );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("關閉連接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


public class server {
    //public static int portNo = 3333;
    public static int portNob=14444;
    public static void main(String[] args) throws IOException {
// TODO Auto-generated method stub
//初始化serverSocket类
        //ServerSocket s = new ServerSocket(portNo);
        ServerSocket s=new ServerSocket(portNob);
        System.out.println("啓動服務器");
//建立socket连接(阻塞，直到有客户端连接)
        //Socket socket = s.accept();
        int i=1;
        while (true){
            System.out.println("等待连接"+i++);
            Socket socket=s.accept();
            System.out.println(socket.getInputStream());
            System.out.println(socket.getOutputStream());
            System.out.println(socket.getLocalPort());
            System.out.println(socket.getPort());
            System.out.println(socket.getInetAddress());
            System.out.println(socket.getRemoteSocketAddress());
            System.out.println(socket.getLocalAddress());
            System.out.println(socket.getLocalSocketAddress());
            new aThread(socket).start();
        }
    }
}
