
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class FileLoadUI {
    private JFrame fr = new JFrame("Cat vs Dog");
    private Container c;
    private JLabel imgLabel = new JLabel();
    private JLabel infoLabel = new JLabel("사진 파일을 불러와주십시오.");

    private JPanel infoPanel = new JPanel();
    private JPanel imgPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private JButton sendImgButton = new JButton("Send Image");
    private JButton loadImgButton = new JButton("Load Image");

    private JFileChooser chooser = new JFileChooser();
    // private JFrame loadingWindow;

    private String path_name;
    private String result = "test";

    FileLoadUI() {
        fr.setSize(400,500);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setBackground(Color.WHITE);
        fr.setLayout(new BorderLayout());

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);

        imgPanel.setBackground(Color.WHITE);
        imgPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout());

        /* 이미지 로드 버튼 생성 */

        loadImgButton.setSize(100, 50);
        loadImgButton.setBackground(Color.WHITE);
        loadImgButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        loadImgButton.addActionListener(new FileOpenActionListener());
        buttonPanel.add(loadImgButton);

        sendImgButton.setSize(100, 50);
        sendImgButton.setBackground(Color.WHITE);
        sendImgButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        sendImgButton.addActionListener(new FileSendActionListener());
        buttonPanel.add(sendImgButton);
        sendImgButton.setVisible(false);

        infoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        infoPanel.add(infoLabel);

        fr.add(infoPanel, BorderLayout.NORTH);
        fr.add(imgPanel, BorderLayout.CENTER);
        fr.add(buttonPanel, BorderLayout.SOUTH);


        // loadingWindow = new LoadingWindow();

        Dimension frameSize = fr.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fr.setLocation((screenSize.width - frameSize.width) /2, (screenSize.height - frameSize.height) /2);
        fr.setResizable(false);
        fr.setVisible(true);
    }

    class FileOpenActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ImageIcon temp;
            int ret = chooser.showOpenDialog(null);
            if(ret != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                        "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            sendImgButton.setVisible(true);

            String original_filePath = chooser.getSelectedFile().getPath();
            // System.out.println(original_filePath);
            temp = new ImageIcon(original_filePath);
            Image im = temp.getImage(); //뽑아온 이미지 객체 사이즈를 새롭게 만들기!

            // 가로 기준으로 설정
            int imageWidth = im.getWidth(null);
            int imageHeight = im.getHeight(null);
            double ratio = (double)400/(double)imageWidth;
            int w = (int)(imageWidth * ratio);
            int h = (int)(imageHeight * ratio);
            Image im2 = im.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon icon2 = new ImageIcon(im2);
            imgLabel.setIcon(icon2);

            imgPanel.add(imgLabel, BorderLayout.CENTER);
            infoLabel.setText("다음 사진을 전송합니다.");
            /* 파일 전송 부분 */

            path_name = new String(original_filePath);

            fr.pack();
        }
    }

    class FileSendActionListener implements ActionListener {
        FileSendActionListener() {}

        public void actionPerformed(ActionEvent a) {
            try {
                Socket c_socket = new Socket("127.0.0.1", 8888);

                loadImgButton.setEnabled(false);
                sendImgButton.setEnabled(false);

                System.out.println("Starting to send Image file.");

                InputStream in = c_socket.getInputStream();
                InputStreamReader ird = new InputStreamReader(in);
                BufferedReader brd = new BufferedReader(ird);

                OutputStream out = c_socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);

                System.out.println("File Name : " + path_name);
                dos.writeUTF(path_name);

                //서버프로그램이 실행되는 컴퓨터에 파일폴더로 사용할 폴더 생성.
                FileInputStream fin = new FileInputStream(path_name);

                byte[] dataBuff = new byte[10000];
                int length = fin.read(dataBuff);
                while (length != -1) {
                    // System.out.println(length + " | " + dataBuff);
                    dos.write(dataBuff, 0, length);
                    length = fin.read(dataBuff);
                }

                /*
                while(true){ //FileInputStream을 통해 파일을 읽어들여서 소켓의 출력스트림을 통해 출력.
                    int data=fin.read();
                    System.out.println(data);
                    if(data == -1) break;
                    dos.write(data);
                }
                */

                System.out.println("Reading Result...");
                result = brd.readLine();
                System.out.println("Result : " + result);

                if(result.equals("Cat") || result.equals("Dog"))
                    infoLabel.setText("분석결과 : " + result + "입니다.");
                else
                    infoLabel.setText(result + " : 에러가 발생했습니다.");

                loadImgButton.setEnabled(true);
                sendImgButton.setEnabled(true);
                //스트림 , 소켓 닫기
                fin.close();
                dos.close();
                brd.close();
                ird.close();
                in.close();
                out.close();
                c_socket.close();
            }catch (ConnectException e){
                System.out.println("Socket Connect Error.");
                JOptionPane.showMessageDialog(null, "연결에 실패했습니다.\n프로그램을 재기동하여 서버 주소와 포트를 확인한 후 다시 입력해주십시오.",
                        "소켓 통신 에러", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } catch (IOException e) {
                loadImgButton.setEnabled(true);
                sendImgButton.setEnabled(true);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
