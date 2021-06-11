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
    public void run() {
        try {
            //데이터를 통신을 위해서 소켓의 스트림 얻기.
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            String fileNameStr = dis.readUTF(); //클라이언트로부터 파일명얻기

            System.out.println("Requested File :" + fileNameStr);
            System.out.println("Request File to Client.");
            System.out.println("Get file data from Client.");
            int lastIndex = fileNameStr.lastIndexOf("\\"); // 마지막으로 \가 나타나는 지점을 찾음
            String file_name = fileNameStr.substring(lastIndex+1);
            String path_name = new String("C:\\Users\\Public\\" + file_name);
            FileOutputStream fos = new FileOutputStream(path_name);

            System.out.println("Starting to File Transfer...");

            byte[] dataBuff = new byte[10000];
            int length = dis.read(dataBuff);
            System.out.print("[■");
            while (length != -1) {
                fos.write(dataBuff, 0, length);
                if(length < 10000) {
                    System.out.println("■]");
                    break;
                } else {
                    length = dis.read(dataBuff);
                    System.out.print("■");
                }
            }

            System.out.println("File Transfer completed.");

            try {
                System.out.println("Starting connection to Docker : " + path_name);
                String[] cmdAry= {"python", "prediction.py", path_name};
                Runtime.getRuntime().exec(cmdAry);
                Thread.sleep(5000); // timeout 개념으로 생각
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Docker connection completed.");
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
                filereader.close();

                if(file.exists()) { // 결과 중복 방지를 위해 읽은 파일은 다시 삭제
                    if(file.delete()){
                        System.out.println("Successfully Deleted File.");
                    } else {
                        System.out.println("Error to Delete file.");
                    }
                } else {
                    System.out.println("Already File is deleted.");
                }

                // Receive를 받는 쓰레드에서 바로 처리
                PrintWriter sendWriter = new PrintWriter(socket.getOutputStream());
                System.out.println("Start to send result to Client... >>> " + result);
                // System.out.println("Sending : " + result);
                sendWriter.println(result);
                sendWriter.flush();
                System.out.println("Successfully sent result.");
            } catch (FileNotFoundException e) {
                PrintWriter sendWriter = new PrintWriter(socket.getOutputStream());
                System.out.println("Start to send result to Client... >>> " + result);
                // System.out.println("Sending : " + result);
                sendWriter.println("ERROR");
                sendWriter.flush();
                System.out.println("Successfully sent result.");
            } catch(IOException e) {
                System.out.println(e);
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("UnknownHostException Detected.");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("IOException Detected.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error Detected.");
            e.printStackTrace();
        }
    }

    public void setSocket(Socket _socket) {
        socket = _socket;
    }

    public String getResult() {
        return result;
    }
}
