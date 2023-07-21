package org.sp.app0721.network.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//서버에 접속하는 각각의 클라이언트마다 1:1 대응하는 대화 전용 쓰레드 정의
public class MessageThread extends Thread{
	Socket socket;
	BufferedReader buffr; //버퍼처리된 문자기반 입력스트림
	BufferedWriter buffw; //버퍼처리된 문자기반 출력스트림
	
	public MessageThread(Socket socket) {
		this.socket=socket;
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//듣기
	public void listen() {
		String msg=null;
		try {
			msg=buffr.readLine(); //듣기
			sendMsg(msg); //다시 보내기
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//말하기
	public void sendMsg(String msg) {
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//쓰레드는 run() 메서드의 닫는 브레이스를 만나면 소멸되므로, 
	//죽이지 않으려면 무한루프로 처리해야 함
	public void run() {
		while(true) {
			listen();
		}
	}
}













