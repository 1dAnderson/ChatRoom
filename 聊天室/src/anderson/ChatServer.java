package anderson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
 
public class ChatServer {
 
	 private List<Client> clients = new ArrayList<>();
	 static int numClient=1;
	 /**
	 * 功能：启动ChatSever 
	 */
	 public static void main(String[] args) {
	 new ChatServer().init();
	 }
	 
	 /**
	 * 功能：对ChatServer初始化 
	 */
	 private void init() {
		 System.out.println("服务器已开启");
		 // BindException
		 ServerSocket ss = null;
		 Socket socket = null;
		 try {
			 ss = new ServerSocket(4711);
		 } catch (BindException e) {
			 
			 System.out.println("端口已被占用");
			 e.printStackTrace();
		 } catch (IOException e1) {
			 e1.printStackTrace();
		 }
		 try {
			 Client client = null;
			 while (true) {
			 socket = ss.accept();			//新用户加入
			 client = new Client(socket);
			 clients.add(client);
			 numClient++;
			 new Thread(client).start();
			 }
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 	}
	/**
	 * 
	 * 实现Client类，接受并转发消息
	 *
	 */
	 private class Client implements Runnable {
		 //建立流处理
		 private Socket socket = null;
		 InputStream in = null;
		 DataInputStream din = null;
		 OutputStream out = null;
		 DataOutputStream dos = null;
		 boolean flag = true;
		 
		 //Client构造函数，实现输入流
		 public Client(Socket socket) {
			 this.socket = socket;
			 try {
				 in = socket.getInputStream();
				 din = new DataInputStream(in);
			 } catch (IOException e) {
				 System.out.println("接受消息失败");
				 e.printStackTrace();
			 }
	 
		 }
		 //实现run函数，发送消息
		 public void run() {
			 String message;				
			 try {
				 while (flag) {
					 message = din.readUTF();
					 forwordToAllClients(message);				
			 	}
			 } catch (SocketException e) {
				 flag = false;
				 clients.remove(this);
				 numClient--;
			 } catch (EOFException e) {
				 flag = false;
				 clients.remove(this);
				 numClient--;
			 } catch (IOException e) {
				 flag = false;
				 System.out.println("接受消息失败");
				 clients.remove(this);
				 numClient--;
				 e.printStackTrace();
			 }
	 
			 if (din != null) {
				 try {
					 din.close();
				 } catch (IOException e) {
					 System.out.println("din关闭失败");
					 e.printStackTrace();
			 		}
			 }
			 if (in != null) {
				 try {
					 in.close();
				 } catch (IOException e) {
					 System.out.println("din关闭失败");
					 e.printStackTrace();
				 }
			 }
			 if (socket != null) {
				 try {
					 socket.close();
				 } catch (IOException e) {
					 System.out.println("din关闭失败");
					 e.printStackTrace();
				 }
			 }
	 
	 }
	 
	 /**
	  * 功能：转发给所有客户端
	  */
		 private void forwordToAllClients(String message) throws IOException {
			 for (Client c : clients) {
				 if (c != this) {
					 out = c.socket.getOutputStream();
					 dos = new DataOutputStream(out);
					 forwordToClient(message);
				 }
			 }
		 }
	 
	 /**
	  * 功能：发送给一个客户端
	  */
		 private void forwordToClient(String message) throws IOException {
			 dos.writeUTF(message);
			 dos.flush();
			 System.out.println("转发成功！");
		 }
	 
	 	}
}
