package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {
	public static void main(String[] args)
	{
		int port = 8888;
		try {
			ServerSocket s_socket = new ServerSocket(port);
			System.out.println("This server is listening... (Port: " + port  + ")");
			while(true) {
				Socket c_socket = s_socket.accept(); //새로운 연결 소켓 생성 및 클라이언트 접속 대기

				FileTransferReceiver ftr = new FileTransferReceiver();
				ftr.setSocket(c_socket);
				ftr.start();
				// while(ftr.getResult() == null);
				/*
				System.out.println("Start to send result to Client...");
				SendThread send_thread = new SendThread();
				send_thread.setSocket(c_socket);
				send_thread.setResult(ftr.getResult());
				send_thread.start();
				*/
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
