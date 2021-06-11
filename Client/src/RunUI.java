import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class RunUI {
    private JFrame fr = new JFrame("Welcome!");
    private JLabel infoLabel = new JLabel("IP 주소와 Port를 입력하십시오.");

    private JPanel infoPanel = new JPanel();
    private JPanel linePanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private JTextField inputIP = new JTextField("127.0.0.1",16);
    private JTextField inputPort = new JTextField("8888",5);

    private JButton setButton = new JButton("입력");

    private String address = "127.0.0.1";
    private String str_temp;
    private int int_temp;
    private int port = 8888;

    RunUI() {
        fr.setSize(400,150);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setBackground(Color.WHITE);
        fr.setLayout(new BorderLayout());

        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new FlowLayout());
        linePanel.setBackground(Color.WHITE);
        linePanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout());

        linePanel.add(inputIP);
        linePanel.add(inputPort);

        setButton.setSize(100, 50);
        setButton.setBackground(Color.WHITE);
        setButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        setButton.addActionListener(new SettingActionListener());
        buttonPanel.add(setButton);

        infoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        infoPanel.add(infoLabel);

        fr.add(infoPanel, BorderLayout.NORTH);
        fr.add(linePanel, BorderLayout.CENTER);
        fr.add(buttonPanel, BorderLayout.SOUTH);

        Dimension frameSize = fr.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fr.setLocation((screenSize.width - frameSize.width) /2,
                (screenSize.height - frameSize.height) /2);
        fr.setResizable(false);
        fr.setVisible(true);

    }

    public static void main(String[] args) {
        new RunUI();
    }

    class SettingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FileLoadUI ui = new FileLoadUI();

            address = inputIP.getText();
            str_temp = inputPort.getText();

            if(!Pattern.matches("((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])([.](?!$)|$)){4}", address)) {
                JOptionPane.showMessageDialog(null,
                        "주소 양식을 확인해주십시오.",
                        "주소 에러", JOptionPane.ERROR_MESSAGE);
                ui.dispose();
                return;
            }

            ui.setAddress(address);

            try {
                    int_temp = Integer.parseInt(str_temp);
                    port = int_temp;
                    if(port < 0 || port > 65535) {
                        JOptionPane.showMessageDialog(null,
                                "Port 양식을 확인해주십시오.",
                                "Port 에러", JOptionPane.ERROR_MESSAGE);
                        ui.dispose();
                        return;
                    }
            } catch(NumberFormatException a) {
                JOptionPane.showMessageDialog(null,
                        "Port 양식을 확인해주십시오.",
                        "Port 에러", JOptionPane.ERROR_MESSAGE);
                ui.dispose();
                return;
            }
            ui.setPort(port);

            ui.setVisible(true);
            fr.dispose();
        }
    }
}
