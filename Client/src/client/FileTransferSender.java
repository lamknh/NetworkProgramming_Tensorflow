import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileTransferSender extends Thread{
    private Socket socket;
    private String fileName;
    private String result = "default result";

    public void run() {
        try {
            JFrame loadingWindow = new LoadingWindow();
            InputStream in = socket.getInputStream();
            InputStreamReader ird = new InputStreamReader(in);
            BufferedReader brd = new BufferedReader(ird);

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
            loadingWindow.dispose();

            /*ReceiveThread 등등 data 받는 부분*/

            //스트림 , 소켓 닫기
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class LoadingWindow extends JFrame {
        // 버튼이 눌러지면 만들어지는 새 창을 정의한 클래스
        LoadingWindow() {
            setTitle("Loading...");
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            // 주의, 여기서 setDefaultCloseOperation() 정의를 하지 말아야 한다
            // 정의하게 되면 새 창을 닫으면 모든 창과 프로그램이 동시에 꺼진다

            JPanel NewWindowContainer = new JPanel();
            setContentPane(NewWindowContainer);

            JLabel NewLabel = new JLabel("결과를 전송하는 중...");

            NewWindowContainer.add(NewLabel);

            setSize(400,100);
            setResizable(false);
            setVisible(true);
        }
    }
    public String getResult() {return result;}
    public void setFileName(String _name)
    {
        fileName = _name;
    }
    public void setSocket(Socket _socket)
    {
        socket = _socket;
    }
}
