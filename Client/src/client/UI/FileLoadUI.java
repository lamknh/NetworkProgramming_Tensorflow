import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileLoadUI extends JFrame{
    Container c;
    JLabel imgLabel = new JLabel();

    FileLoadUI() {
        setTitle("TestTitle");
        setSize(300,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());

        JButton loadImgButton = new JButton("Load Image");
        loadImgButton.setSize(100, 50);
        loadImgButton.addActionListener(new OpenActionListener());
        this.add(loadImgButton, BorderLayout.CENTER);

        c = getContentPane();
        c.add(imgLabel);

        setVisible(true);
    }

    class OpenActionListener implements ActionListener {
        JFileChooser chooser;
        OpenActionListener() {
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

            String filePath = chooser.getSelectedFile().getPath();
            imgLabel.setIcon(new ImageIcon(filePath));
            pack();
        }
    }

}
