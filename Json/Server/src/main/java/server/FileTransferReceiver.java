package server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class FileTransferReceiver extends Thread{
    private Socket socket;
    private String json = "{\"Dog\": 0.9, \"Cat\": 0.1 }";
    private String result = "";

    @Override
    public void run(){
        try {
            //데이터를 통신을 위해서 소켓의 스트림 얻기.
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            String fileNameStr = dis.readUTF(); //클라이언트로부터 파일명얻기

            System.out.println("Requested File :"+fileNameStr);
            System.out.println("Request File to Client");
            System.out.println("Get file data from Client");
            int lastIndex = fileNameStr.lastIndexOf("\\"); // 마지막으로 \가 나타나는 지점을 찾음
            String file_name = fileNameStr.substring(lastIndex+1);
            String path_name = new String("C:\\Users\\Public\\" + file_name);
            FileOutputStream fos = new FileOutputStream(path_name);
            // BufferedOutputStream bos = new BufferedOutputStream(fos);
            // byte[] buffer = new byte[1024];

            System.out.println("Starting to File Transfer");
            while(true){
                int data=dis.read(/*buffer*/);
                if(data == -1) break;
                fos.write(data);
                //bos.write(buffer,0,data);
            }

            System.out.println("File Transfer completed");

            // Server에서 Docker랑 통신하는 부분
            try {
                System.out.println("Starting connection to Docker");
                // String[] cmdAry= {"python", "request_ml_server.py"};
                // Runtime.getRuntime().exec(cmdAry);
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Docker connection completed");

            // Client로 result 전송
            JsonParser parser = new JsonParser(); // 파서 준비
            Object obj = parser.parse(json); // 파싱 수행
            JsonObject jsonObj = (JsonObject) obj;

            //System.out.println(jsonObj.get("Dog"));
            //System.out.println(jsonObj.get("Cat"));

            float dogResult = jsonObj.get("Dog").getAsFloat();
            float catResult = jsonObj.get("Cat").getAsFloat();

            if(dogResult > catResult){
                result = "Dog";
            }else{
                result = "Cat";
            }

            System.out.println("FTR :" + result);

            //스트림 , 소켓 닫기 X
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

    public String getResult(){
        return result;
    }
}