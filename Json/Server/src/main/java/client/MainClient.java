package client;

import java.io.IOException;
import java.net.Socket;

import java.io.File;
import java.util.Scanner;

public class MainClient {
	
	public static void main(String[] args)
	{
		try {
			Socket c_socket = new Socket("127.0.0.1", 8888);
			if(!c_socket.isConnected()){
				System.out.println("Socket Connect Error.");
				System.exit(0);
			}

			FileTransferSender fts = new FileTransferSender();
			fts.setSocket(c_socket);

			fts.start();

//			ReceiveThread rec_thread = new ReceiveThread();
//			rec_thread.setSocket(c_socket);
//			rec_thread.start();
//
//			SendThread send_thread = new SendThread();
//			send_thread.setSocket(c_socket);
//
//			send_thread.start();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
