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
	 private JButton send = new JButton("����");
	 //������
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

	 //���ܣ��Դ��ڽ��г�ʼ��
	 ClientFrame(String ip,int port,String fn,String un) throws IOException{
		 ClientFrame.fileName = fn;
		 ClientFrame.userName = un;
		 // ��ӿؼ�
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
		 //�ָ������¼
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
	     //��Ļ�Զ������������ı�
	     javax.swing.text.Document doc = textAreaContent.getDocument();
	     textAreaContent.setCaretPosition(doc.getLength());
		 // �ر��¼�
		 addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e){
				 System.out.println("�û���ͼ�رմ���");
				 disconnect();
				 System.exit(0);
	  }
	  });
		 // textFieldContent��ӻس��¼�
		 textFieldContent.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 try {
					onClickEnter();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
			 }
		 });
		 //textFieldContent��ӷ����¼�
		 send.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 try {
					onClickEnter();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
			 }
		 });
	 
		 // ��������
		 connect(ip,port);
		 new Thread(new ReciveMessage()).start();
	 }
/**
 * 	 
 * ReceiveMessage�࣬������Ϣ
 *
 */
private class ReciveMessage implements Runnable {
	 @Override
	 public void run() {
		 flag = true;
		 try {
			 //������Ϣ���沢��ʾ
			 while (flag) {
				 String message = dis.readUTF();
				 String msg = message + "\n"+"\n";
				 saveMessage(msg);
				 textAreaContent.append(msg);
				 //��Ļ�Զ������������ı�
				 javax.swing.text.Document doc = textAreaContent.getDocument();
			     textAreaContent.setCaretPosition(doc.getLength());
			 }
		 } catch (EOFException e) {
			 flag = false;
			 System.out.println("�ͻ����ѹر�");
		 } catch (SocketException e) {
			 flag = false;
			 System.out.println("�ͻ����ѹر�");
		 } catch (IOException e) {
			 flag = false;
			 System.out.println("������Ϣʧ��");
		 }
	 }
	 
	}
	 
	//���ܣ�������س�ʱ�������¼�
	 public void onClickEnter() throws IOException {
		 String message = textFieldContent.getText().trim();
		 if (message != null && !message.equals("")) {
			 String time = String.valueOf(LocalDateTime.now());
			 String msg = "��"+"("+time +"):" + "\n" + message + "\n"+'\n'; //��װ��ʾ���Լ���Ļ����Ϣ
			 saveMessage(msg);
			 textAreaContent.append(msg);
			 textFieldContent.setText("");
			 sendMessageToServer(message);
			 //��Ļ�Զ������������ı�
			 javax.swing.text.Document doc = textAreaContent.getDocument();
		     textAreaContent.setCaretPosition(doc.getLength());
			 
	 }
	 }
	 
	 //���ܣ���������������Ϣ
	 public void sendMessageToServer(String message) {
		 try {
			 String time = String.valueOf(LocalDateTime.now());
			 String msg = userName+"("+time+")"+":"+'\n'+message; //��װ���͵���Ϣ
			 dos.writeUTF(msg);
			 dos.flush();
		 } catch (IOException e) {
			 System.out.println("������Ϣʧ��");
			 e.printStackTrace();
		 }
	 }
	 //���ܣ�������Ϣ	 
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
	 
	 //���ܣ���������
	 private void connect(String ip,int port) {
		 try {
			
			 socket = new Socket(ip, port);
			 out = socket.getOutputStream();
			 dos = new DataOutputStream(out);
			 in = socket.getInputStream();
			 dis = new DataInputStream(in);
			 String inMsg = "********************"+userName+"����Ⱥ�ģ���ӭ��"+"********************"; //����Ⱥ����Ϣ����
			 sendMessageToServer(inMsg);
		 } catch (UnknownHostException e) {
			 System.out.println("��������ʧ��");
			 e.printStackTrace();
		 } catch (IOException e) {
			 System.out.println("��������ʧ��");
			 e.printStackTrace();
		 }
	 }
	 
	 //���ܣ��ر���������
	 private void disconnect() {
		 flag = false;
		 String outMsg = "********************"+userName+"�˳�Ⱥ��"+"********************"; //�˳�Ⱥ����Ϣ����
		 sendMessageToServer(outMsg);
		 if (dos != null) {
			 try {
				 dos.close();
			 } catch (IOException e) {
				 System.out.println("dos�ر�ʧ��");
				 e.printStackTrace();
			 }
		 }
		 if (out != null) {
			 try {
				 out.close();
			 } catch (IOException e) {
				 System.out.println("dos�ر�ʧ��");
				 e.printStackTrace();
			 }
		 }
		 if (socket != null) {
			 try {
				 socket.close();
			 } catch (IOException e) {
				 System.out.println("socket�ر�ʧ��");
				 e.printStackTrace();
			 }
		 }
	 }
	 
}