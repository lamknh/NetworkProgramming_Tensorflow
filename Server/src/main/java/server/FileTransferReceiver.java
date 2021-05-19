package server;
import java.io.*;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import ai.djl.ndarray.*;
import ai.djl.ndarray.types.Shape;

public class FileTransferReceiver extends Thread{
    private Socket socket;

    @Override
    public void run(){
        try {
            Scanner s = new Scanner(System.in);
            
            try(NDManager manager = NDManager.newBaseManager()) {
            	NDArray nd = manager.ones(new Shape(2, 3));
            	System.out.println(nd);
            }
            
            //데이터를 통신을 위해서 소켓의 스트림 얻기.
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            
            String fileNameStr = dis.readUTF(); //클라이언트로부터 파일명얻기
            
            System.out.println(fileNameStr);
            System.out.println("Requested File :"+fileNameStr);
            System.out.println("Request File to Client");
            System.out.println("Get file data from Client");
            FileOutputStream fos = new FileOutputStream("/Users/yong/pLibrary/NP_maven_tensorflow/src/main/java/server/" + fileNameStr); // 자신의 프로젝트 경로에 맞게 수정
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
