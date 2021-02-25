package anderson;
import anderson.ClientFrame;

import java.io.IOException;
/**
 * 
 * 自定义用户
 *
 */
public class User1 {
	static String ip = "127.0.0.1";
	static int port = 4711;
	static String fileName = "User1.txt";
	static String userName = "信息学院扛把子";
	public static void main(String args[]) throws IOException{
		new ClientFrame(ip,port,fileName,userName);
	}
}
