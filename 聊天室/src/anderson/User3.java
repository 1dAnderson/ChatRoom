package anderson;
import anderson.ClientFrame;

import java.io.IOException;
/**
 * 
 * 自定义用户
 *
 */
public class User3 {
	static String ip = "127.0.0.1";
	static int port = 4711;
	static String fileName = "User3.txt";
	static String userName = "冬天不穿秋裤";
	public static void main(String args[]) throws IOException{
		new ClientFrame(ip,port,fileName,userName);
	}
}

