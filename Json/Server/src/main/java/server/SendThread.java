package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread{

	private Socket m_Socket;
	private String result;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			PrintWriter sendWriter = new PrintWriter(m_Socket.getOutputStream());
			System.out.println("sendthread : " + result);
			//String sendString = "TEST MESSAGE";
			sendWriter.println(result);
			sendWriter.flush();
			System.out.println("Successfully sent result.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSocket(Socket _socket)
	{
		m_Socket = _socket;
	}

	public void setResult(String result) {
		this.result = result;
	}
}