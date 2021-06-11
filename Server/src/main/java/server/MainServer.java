package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class MainServer {
	public static void main(String[] args) {
		int port = 8888;
		String str_temp;
		int int_temp;
		Scanner input = new Scanner(System.in);

		System.out.print("Input Server Port Number [8888] >>> ");
		str_temp = input.nextLine();
		if(!str_temp.equals("")) {
			try {
				int_temp = Integer.parseInt(str_temp);
				port = int_temp;
			} catch(NumberFormatException e) {
				System.err.println("Invalid Input format. Please restart and re-input port number.");
				System.exit(1);
			}
		}

		try {
			ServerSocket s_socket = new ServerSocket(port);
			System.out.println("This server is listening... (Port: " + port  + ")");
			while(true) {
				Socket c_socket = s_socket.accept(); //새로운 연결 소켓 생성 및 클라이언트 접속 대기

				FileTransferReceiver ftr = new FileTransferReceiver();
				ftr.setSocket(c_socket);
				ftr.start();
				ftr.join(); // Thread 간 실핼순서를 동기화
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
