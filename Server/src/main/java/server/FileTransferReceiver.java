package server;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class FileTransferReceiver extends Thread{
    private Socket socket;
    private String result;

    @Override
    public void run(){
        try {
            //데이터를 통신을 위해서 소켓의 스트림 얻기.
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

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


            byte[] dataBuff = new byte[10000];
            int length = dis.read(dataBuff);
            while (length != -1) {
                System.out.print(".");
                System.out.println(length + " | " + dataBuff);
                fos.write(dataBuff, 0, length);
                if(length < 10000) break;
                else length = dis.read(dataBuff);
            }

            System.out.println("File Transfer completed");

            // Server에서 Docker랑 통신하는 부분
            try {
                System.out.println("Starting connection to Docker : " + path_name);
                String[] cmdAry= {"python", "prediction.py", path_name};
                Runtime.getRuntime().exec(cmdAry);
                Thread.sleep(5000); // timeout 개념으로 생각
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Docker connection completed");
            // Client로 result 전송
            //스트림 , 소켓 닫기
            String line = "";
            try{
                //파일 객체 생성
                File file = new File("C:\\Users\\Public\\result.txt");
                //입력 스트림 생성
                FileReader filereader = new FileReader(file);
                //입력 버퍼 생성
                BufferedReader bufReader = new BufferedReader(filereader);
                while((line = bufReader.readLine()) != null){
                    System.out.println(line);
                    result = line;
                }
                //.readLine()은 끝에 개행문자를 읽지 않는다.
                bufReader.close();

                System.out.println("Start to send result to Client... >>>" + result);
                SendThread send_thread = new SendThread();
                send_thread.setSocket(socket);
                send_thread.setResult(result);
                send_thread.start();
            }catch (FileNotFoundException e) {
                System.out.println("Start to send Error... >>>" + result);
                SendThread send_thread = new SendThread();
                send_thread.setSocket(socket);
                send_thread.setResult("ERROR");
                send_thread.start();
            }catch(IOException e){
                System.out.println(e);
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("UnknownHostException Detected");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("IOException Detected");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error Detected");
        }
    }
    public void setSocket(Socket _socket)
    {
        socket = _socket;
    }
    public String getResult() {
        return result;
    }
}
