package Iotry;

import iofile.ClientFile;
import iofile.ServerFile;
import iofile.SocketFileJFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class input   extends JFrame{
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JProgressBar progressBar_2;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    input frame = new input();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public input(){
        //ui
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(SocketFileJFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/newFolder.gif")));
        setForeground(Color.WHITE);

        setResizable(false);
        setTitle("局域网文件传输 V1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;//获取分辨率宽
        int heiht = Toolkit.getDefaultToolkit().getScreenSize().height;//获取分辨率高

        //分辨率宽高减去软件的宽高除以2把软件居中显示
        setBounds((width - 747) / 2, (heiht - 448) / 2, 738, 372);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        //文件传输端
        JPanel panel_2 = new JPanel();
        panel_2.setToolTipText("");
        panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "文件传输",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel_2.setBackground(Color.WHITE);
        panel_2.setBounds(39, 21, 652, 252);
        contentPane.add(panel_2);
        panel_2.setLayout(null);

        //文件传输端的进度条
        JProgressBar progressBar_1 = new JProgressBar();
        progressBar_1.setFont(new Font("宋体", Font.PLAIN, 18));
        progressBar_1.setStringPainted(true);
        progressBar_1.setForeground(new Color(240, 128, 128));
        progressBar_1.setBackground(Color.WHITE);
        progressBar_1.setBounds(96, 169, 472, 20);
        panel_2.add(progressBar_1);


        //IP地址标签
        JLabel lblIp = new JLabel("传输对象:");
        lblIp.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
        lblIp.setBounds(26, 44, 100, 27);
        panel_2.add(lblIp);
        //IP地址文本框
        textField_1 = new JTextField();
//        textField_1.setText("127.0.0.1");
        textField_1.setText("注册名xxx");
        textField_1.setFont(new Font("新宋体", Font.PLAIN, 20));
        textField_1.setBounds(120, 44, 184, 30);
        panel_2.add(textField_1);
        textField_1.setColumns(10);

        //路径选择
        JLabel label_2 = new JLabel("打开文件:");
        label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
        label_2.setBounds(320, 50, 144, 18);
        panel_2.add(label_2);
        //打开本地路径的按钮
        JButton button = new JButton("...");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jf.showOpenDialog(input.this);
                textField_3.setText(jf.getSelectedFile().getPath());
            }
        });
        button.setBackground(Color.WHITE);
        button.setBounds(400, 51, 35, 20);
        panel_2.add(button);



        //显示文件路径框
        textField_3 = new JTextField();
        textField_3.setEnabled(false);
        textField_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
        textField_3.setBounds(150, 100, 260, 30);
        panel_2.add(textField_3);
        textField_3.setColumns(10);

        JLabel lblNewLabel = new JLabel("文件路径:");
        lblNewLabel.setFont(new Font("等线 Light", Font.PLAIN, 20));
        lblNewLabel.setBounds(33, 97, 156, 38);
        panel_2.add(lblNewLabel);


        //确定按钮
        JButton button_1 = new JButton("确认");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

//                ClientFile client = new ClientFile(textField_1.getText(), Integer.parseInt(textField_2.getText()), textField_3.getText(), progressBar_1);
//                改为：new ClientFile(ip, port， 路径, 进度条1);
                ClientFile client = new ClientFile("127.0.0.1", 8080 , textField_3.getText(), progressBar_1);
                client.start();
                textField_3.setText("");

            }
        });
        button_1.setForeground(new Color(255, 255, 0));
        button_1.setBackground(new Color(233, 150, 122));
        button_1.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
        button_1.setBounds(475, 100, 91, 38);
        panel_2.add(button_1);
    }

}


