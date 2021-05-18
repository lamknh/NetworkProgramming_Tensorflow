package server;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class FileTransferReceiver extends Thread{
    private Socket socket;

    @Override
    public void run(){
        try {
            Scanner s = new Scanner(System.in);

            //데이터를 통신을 위해서 소켓의 스트림 얻기.
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            String fileNameStr = dis.readUTF(); //클라이언트로부터 파일명얻기

            System.out.println("Requested File :"+fileNameStr);
            System.out.println("Request File to Client");
            System.out.println("Get file data from Client");
            int lastIndex = fileNameStr.lastIndexOf("\\"); // 마지막으로 \가 나타나는 지점을 찾음
            String file_name = fileNameStr.substring(lastIndex+1);
            String path_name = new String("C:\\Users\\Public\\" + file_name);
            FileOutputStream fos = new FileOutputStream(path_name);
            //BufferedOutputStream bos = new BufferedOutputStream(fos);
            //byte[] buffer = new byte[1024];

            while(true){
                int data=dis.read(/*buffer*/);
                if(data == -1) break;
                fos.write(data);
                //bos.write(buffer,0,data);
            }

            System.out.println("File Transfer completed");

            //스트림 , 소켓 닫기
            //fos.close();
            dos.close();
            dis.close();
            out.close();
            in.close();
            socket.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
