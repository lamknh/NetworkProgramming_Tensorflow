package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread{

	private Socket m_Socket;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter sendWriter = new PrintWriter(m_Socket.getOutputStream());
			String sendString;
			
			while(true)
			{
				sendString = tmpbuf.readLine();
				
				sendWriter.println(sendString);
				sendWriter.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSocket(Socket _socket)
	{
		m_Socket = _socket;
	}
	
}
