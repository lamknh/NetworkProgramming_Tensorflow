
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveThread extends Thread{

	private Socket m_Socket;
	private String result = "default";
	private boolean end = false;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Starting to receive");
			InputStream in = m_Socket.getInputStream();
			InputStreamReader ird = new InputStreamReader(in);
			BufferedReader brd = new BufferedReader(ird);

			System.out.println("Reading Result...");
			result = brd.readLine();
			System.out.println("Result : " + result);

			end = true;
			m_Socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getEnd() {return end;}
	public String getResult() { return result;}
	public void setSocket(Socket _socket)
	{
		m_Socket = _socket;
	}

}
