import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileTransferSender extends Thread{
    private Socket socket;
    private String fileName;

    public void run() {
        try {
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            System.out.println("File Name : " + fileName);
            dos.writeUTF(fileName);

            //서버프로그램이 실행되는 컴퓨터에 파일폴더로 사용할 폴더 생성.
            FileInputStream fin = new FileInputStream(fileName);
            while(true){ //FileInputStream을 통해 파일을 읽어들여서 소켓의 출력스트림을 통해 출력.
                int data=fin.read();
                if(data == -1) break;
                dos.write(data);
            }

            //스트림 , 소켓 닫기
            fin.close();
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setFileName(String _name)
    {
        fileName = _name;
    }
    public void setSocket(Socket _socket)
    {
        socket = _socket;
    }
}
