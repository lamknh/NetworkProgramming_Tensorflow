package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class FileTransferSender extends Thread{
    private Socket socket;

    @Override
    public void run() {
        try {
            Scanner s = new Scanner(System.in);

            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            System.out.print("File Name : ");
            String fileNameMsg = s.nextLine();
            dos.writeUTF(fileNameMsg);

            //서버프로그램이 실행되는 컴퓨터에 파일폴더로 사용할 폴더 생성.
            FileInputStream fin = new FileInputStream("D:\\"+fileNameMsg);
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

    public void setSocket(Socket _socket)
    {
        socket = _socket;
    }
}
