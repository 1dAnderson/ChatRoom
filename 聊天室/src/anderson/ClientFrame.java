package anderson;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import javax.swing.*;

import anderson.MyTextArea;
import anderson.MyTextField;

public class ClientFrame extends Frame {
	
	 private MyTextField textFieldContent = new MyTextField();
	 private MyTextArea textAreaContent = new MyTextArea(true);
	 private JPanel panel1 = new JPanel();
	 private JPanel panel2 = new JPanel();
	 private JButton send = new JButton("发送");
	 //滚动条
	 JScrollPane myScrollPane2 = new JScrollPane(textAreaContent);
	 JSplitPane jSplitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,null,
	            myScrollPane2);
	 
	 private Socket socket = null;
	 private OutputStream out = null;
	 private DataOutputStream dos = null;
	 private InputStream in = null;
	 private DataInputStream dis = null;
	 private boolean flag = false;
	 static private String fileName = null;
	 static private String userName = null; 

	 //功能：对窗口进行初始化
	 ClientFrame(String ip,int port,String fn,String un) throws IOException{
		 ClientFrame.fileName = fn;
		 ClientFrame.userName = un;
		 // 添加控件
		 this.setVisible(true);
		 this.setSize(700,800);
		 setTitle("ChatRoom");
		 panel1.setLayout(new BorderLayout());
	     panel1.add(jSplitPane,BorderLayout.CENTER);
	     
	     panel2.setLayout(new BorderLayout());
	     panel2.add(textFieldContent);
	     panel2.add(send,BorderLayout.EAST);
	     this.setLayout(new BorderLayout());
	     this.add(panel1,BorderLayout.CENTER);
	     this.add(panel2,BorderLayout.SOUTH);
		 textAreaContent.setFocusable(false);
		 //恢复聊天记录
		 String path = System.getProperty("user.dir");
		 File file = new File(path);
		 File file1 = new File(file,fileName);
		 if(!file1.exists()) {
			 file1.createNewFile();
		 }
		 BufferedReader reader = new BufferedReader(new FileReader(file1));
	        String line;
	        while ((line=reader.readLine())!=null)
	        	textAreaContent.append(line+'\n');
	     //屏幕自动滚动到最新文本
	     javax.swing.text.Document doc = textAreaContent.getDocument();
	     textAreaContent.setCaretPosition(doc.getLength());
		 // 关闭事件
		 addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e){
				 System.out.println("用户试图关闭窗口");
				 disconnect();
				 System.exit(0);
	  }
	  });
		 // textFieldContent添加回车事件
		 textFieldContent.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 try {
					onClickEnter();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			 }
		 });
		 //textFieldContent添加发送事件
		 send.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 try {
					onClickEnter();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			 }
		 });
	 
		 // 建立连接
		 connect(ip,port);
		 new Thread(new ReciveMessage()).start();
	 }
/**
 * 	 
 * ReceiveMessage类，接受消息
 *
 */
private class ReciveMessage implements Runnable {
	 @Override
	 public void run() {
		 flag = true;
		 try {
			 //接受消息保存并显示
			 while (flag) {
				 String message = dis.readUTF();
				 String msg = message + "\n"+"\n";
				 saveMessage(msg);
				 textAreaContent.append(msg);
				 //屏幕自动滚动到最新文本
				 javax.swing.text.Document doc = textAreaContent.getDocument();
			     textAreaContent.setCaretPosition(doc.getLength());
			 }
		 } catch (EOFException e) {
			 flag = false;
			 System.out.println("客户端已关闭");
		 } catch (SocketException e) {
			 flag = false;
			 System.out.println("客户端已关闭");
		 } catch (IOException e) {
			 flag = false;
			 System.out.println("接受消息失败");
		 }
	 }
	 
	}
	 
	//功能：当点击回车时触发的事件
	 public void onClickEnter() throws IOException {
		 String message = textFieldContent.getText().trim();
		 if (message != null && !message.equals("")) {
			 String time = String.valueOf(LocalDateTime.now());
			 String msg = "你"+"("+time +"):" + "\n" + message + "\n"+'\n'; //包装显示在自己屏幕的消息
			 saveMessage(msg);
			 textAreaContent.append(msg);
			 textFieldContent.setText("");
			 sendMessageToServer(message);
			 //屏幕自动滚动到最新文本
			 javax.swing.text.Document doc = textAreaContent.getDocument();
		     textAreaContent.setCaretPosition(doc.getLength());
			 
	 }
	 }
	 
	 //功能：给服务器发送消息
	 public void sendMessageToServer(String message) {
		 try {
			 String time = String.valueOf(LocalDateTime.now());
			 String msg = userName+"("+time+")"+":"+'\n'+message; //包装发送的消息
			 dos.writeUTF(msg);
			 dos.flush();
		 } catch (IOException e) {
			 System.out.println("发送消息失败");
			 e.printStackTrace();
		 }
	 }
	 //功能：保存消息	 
	 public void saveMessage(String msg) throws IOException {
		 	String path = System.getProperty("user.dir");
		 	File file = new File(path);
		 	File file1 = new File(file,fileName);
		 	if(!file1.exists()) {
				 file1.createNewFile();
			 }
	        BufferedWriter bw = new BufferedWriter(new FileWriter(file1,true));
	        bw.write(msg+'\n');
	        bw.flush();
	        bw.close();

	    }
	 
	 //功能：申请连接
	 private void connect(String ip,int port) {
		 try {
			
			 socket = new Socket(ip, port);
			 out = socket.getOutputStream();
			 dos = new DataOutputStream(out);
			 in = socket.getInputStream();
			 dis = new DataInputStream(in);
			 String inMsg = "********************"+userName+"加入群聊，欢迎！"+"********************"; //加入群聊消息提醒
			 sendMessageToServer(inMsg);
		 } catch (UnknownHostException e) {
			 System.out.println("申请链接失败");
			 e.printStackTrace();
		 } catch (IOException e) {
			 System.out.println("申请链接失败");
			 e.printStackTrace();
		 }
	 }
	 
	 //功能：关闭流和连接
	 private void disconnect() {
		 flag = false;
		 String outMsg = "********************"+userName+"退出群聊"+"********************"; //退出群聊消息提醒
		 sendMessageToServer(outMsg);
		 if (dos != null) {
			 try {
				 dos.close();
			 } catch (IOException e) {
				 System.out.println("dos关闭失败");
				 e.printStackTrace();
			 }
		 }
		 if (out != null) {
			 try {
				 out.close();
			 } catch (IOException e) {
				 System.out.println("dos关闭失败");
				 e.printStackTrace();
			 }
		 }
		 if (socket != null) {
			 try {
				 socket.close();
			 } catch (IOException e) {
				 System.out.println("socket关闭失败");
				 e.printStackTrace();
			 }
		 }
	 }
	 
}