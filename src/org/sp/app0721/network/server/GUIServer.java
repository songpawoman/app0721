package org.sp.app0721.network.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUIServer extends JFrame{
	JPanel p_north;
	JTextField t_port; 
	JButton bt;
	
	JTextArea area;
	JScrollPane scroll;
	
	Thread runThread; //메인 쓰레드를 대기상태에 빠지지 않게 하기 위함 
	ServerSocket server; //대화용이 아닌, 접속자 감지 및 대화용 소켓 얻기 위한 서버소켓
	
	public GUIServer() {
		p_north = new JPanel();
		t_port = new JTextField("7777", 10);
		bt = new JButton("서버가동");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		
		//조립 
		p_north.add(t_port);
		p_north.add(bt);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		setBounds(500,200,300,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runThread = new Thread() {
					public void run() {
						runServer();
					}
				};
				runThread.start(); //Runnable 로 진입
			}
		});
	}
	
	public void runServer() {
		//끊임없이 다수의 클라이언트 접속 허용 및 처리
		int port=Integer.parseInt(t_port.getText()); // "7777"
		try {
			server = new ServerSocket(port); //서버 생성 
			area.append("서버생성\n");
			
			while(true) {
				//접속자가 감지되면, 대화용 소켓이 반환됨
				Socket socket=server.accept(); //접속자가 있을때까지 대기상태에 빠진다...
				String ip=socket.getInetAddress().getHostAddress();
				area.append(ip+"접속\n");
				
				//접속자가 발견되면, 대화용 쓰레드를 생성하면서 소켓을 넘겨주자 
				MessageThread mt = new MessageThread(socket);
				mt.start(); //대화용 쓰레드 동작 시작~!!!!
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new GUIServer();
	}
}








