import java.net.Socket;

public class InetAddress {
    public static void main(String[] args) throws Exception {
        java.net.InetAddress locAdd = null; 		// 声明InetAddress对象
        java.net.InetAddress remAdd = null; 		// 声明InetAddress对象
        locAdd = java.net.InetAddress.getLocalHost();	// 得到本地InetAddress对象
        remAdd = java.net.InetAddress.getByName("www.baidu.com"); 	// 取得远程InetAddress
        System.out.println(java.net.InetAddress.getByName("localhost"));
        System.out.println(java.net.InetAddress.getLoopbackAddress());
        Socket s=new Socket();
        System.out.println(s.getInetAddress());
    }
}