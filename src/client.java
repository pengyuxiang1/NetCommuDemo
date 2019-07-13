import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP实现的Socket通信
 */
public class client{
    public static int portNo=14444;
    public static String myname="2号";

    public static void main(String[] args) throws IOException {
        InetAddress addr = InetAddress.getByName("localhost");
        Socket s=new Socket("10.21.22.116",portNo);//第一个参数是你的服务器的ip号，在cmd终端输入ipconfig，找到ipv4对应的就好了。
        System.out.println(s.getInputStream());
        System.out.println(s.getOutputStream());

        System.out.println("socket=" + s);
        try {
            BufferedReader bufRead=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
            out.println("Hello server ,I am " + myname);
            while (true) {
                String line= bufRead.readLine();
                System.out.println("接收到服务器回复："+line);
                System.out.println("开始输入");
                Scanner scanner=new Scanner(System.in);
                out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            s.close();
        }

    }
}