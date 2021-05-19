
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.Socket;

public class FileLoadUI {
    private JFrame fr = new JFrame("TestTitle");
    private Container c;
    private JLabel imgLabel = new JLabel();
    private JLabel infoLabel = new JLabel("사진 파일을 불러와주십시오.");

    private JPanel infoPanel = new JPanel();
    private JPanel imgPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private String path_name;

    FileLoadUI() {
        fr.setSize(400,500);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setLayout(new BorderLayout());

        imgPanel.setLayout(new BorderLayout());
        infoPanel.setLayout(new FlowLayout());
        buttonPanel.setLayout(new FlowLayout());

        /* 이미지 로드 버튼 생성 */
        JButton loadImgButton = new JButton("Load Image");
        loadImgButton.setSize(100, 50);
        loadImgButton.addActionListener(new FileOpenActionListener());
        buttonPanel.add(loadImgButton);

        infoPanel.add(infoLabel);

        fr.add(infoPanel, BorderLayout.NORTH);
        fr.add(imgPanel, BorderLayout.CENTER);
        fr.add(buttonPanel, BorderLayout.SOUTH);

        fr.setVisible(true);
    }

    class FileOpenActionListener implements ActionListener {
        JFileChooser chooser;
        FileOpenActionListener() {
            chooser = new JFileChooser();
        }
        public void actionPerformed(ActionEvent e) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG & GIF Images", "jpg", "gif");
            chooser.setFileFilter(filter);

            int ret = chooser.showOpenDialog(null);
            if(ret != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                        "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JButton SendImgButton = new JButton("Send Image");
            SendImgButton.setSize(100, 50);
            SendImgButton.addActionListener(new FileSendActionListener());
            buttonPanel.add(SendImgButton);

            String original_filePath = chooser.getSelectedFile().getPath();
            System.out.println(original_filePath);
            imgLabel.setIcon(new ImageIcon(original_filePath));

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
                if(!c_socket.isConnected()){
                    System.out.println("Socket Connect Error.");
                    System.exit(0);
                }

                FileTransferSender fts = new FileTransferSender();
                fts.setSocket(c_socket);
                fts.setFileName(path_name);
                fts.start();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
