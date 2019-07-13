package iofile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTabbedPane;
import java.awt.Panel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SocketFileJFrame extends JFrame {

//    private JPanel contentPane;
//    private JTextField textField;
//    private JTextField textField_1;
//    private JTextField textField_2;
//    private JTextField textField_3;
//    private JProgressBar progressBar_2;
//
//    /**
//     * Launch the application.
//     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    SocketFileJFrame frame = new SocketFileJFrame();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    /**
//     * Create the frame.
//     */
//    public SocketFileJFrame() {
//        //ui
//        setIconImage(Toolkit.getDefaultToolkit()
//                .getImage(SocketFileJFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/newFolder.gif")));
//        setForeground(Color.WHITE);
//
//        setResizable(false);
//        setTitle("局域网文件传输 V1.0");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        int width = Toolkit.getDefaultToolkit().getScreenSize().width;//获取分辨率宽
//        int heiht = Toolkit.getDefaultToolkit().getScreenSize().height;//获取分辨率高
//
//        //分辨率宽高减去软件的宽高除以2把软件居中显示
//        setBounds((width - 747) / 2, (heiht - 448) / 2, 738, 472);
//        contentPane = new JPanel();
//        contentPane.setBackground(Color.WHITE);
//        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//        setContentPane(contentPane);
//        contentPane.setLayout(null);
//
//        JPanel panel_1 = new JPanel();
//        panel_1.setToolTipText("");
//        panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
//                "\u6587\u4EF6\u63A5\u6536\u670D\u52A1\u5668", TitledBorder.LEADING, TitledBorder.TOP, null,
//                new Color(0, 0, 0)));
//        panel_1.setBackground(Color.WHITE);
//        panel_1.setBounds(39, 28, 652, 119);
//        contentPane.add(panel_1);
//        panel_1.setLayout(null);
//
//        JLabel label = new JLabel("\u7AEF\u53E3:");
//        label.setFont(new Font("新宋体", Font.PLAIN, 22));
//        label.setBounds(26, 31, 66, 35);
//        panel_1.add(label);
//
//        //端口文本框
//        textField = new JTextField();
//        textField.setFont(new Font("宋体", Font.PLAIN, 19));
//        textField.setText("8080");
//        textField.setBounds(89, 36, 126, 26);
//        panel_1.add(textField);
//        textField.setColumns(10);
//
//        //服务器关闭启动的按钮
//        JToggleButton tglbtnNewToggleButton = new JToggleButton("\u542F\u52A8\u670D\u52A1\u5668");
//        tglbtnNewToggleButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                if (tglbtnNewToggleButton.isSelected()) {
//                    //如果是按下显示关闭服务器
//                    tglbtnNewToggleButton.setText("关闭服务器");
//                    textField.setEnabled(false);//按下之后 端口文本框要设置不能写入
//                    try {
//                        //启动服务器
//                        ServerFile.openServer(Integer.parseInt(textField.getText())
//                                , progressBar_2);
//                    } catch (Exception e1) {
//                        javax.swing.JOptionPane.showMessageDialog(null, "Exception");
//                    }
//                } else {
//                    //否启动服务器
//                    tglbtnNewToggleButton.setText("启动服务器");
//                    textField.setEnabled(true);////弹起之后 端口文本框要设置可写状态
//                    ServerFile.closeServer();//关闭服务器
//                }
//
//            }
//        });
//        tglbtnNewToggleButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 19));
//        tglbtnNewToggleButton.setBackground(Color.WHITE);
//        tglbtnNewToggleButton.setForeground(Color.DARK_GRAY);
//        tglbtnNewToggleButton.setBounds(345, 34, 138, 28);
//        panel_1.add(tglbtnNewToggleButton);
//
//        //文件接收端的进度条
//        progressBar_2 = new JProgressBar();
//        progressBar_2.setBackground(Color.WHITE);
//        progressBar_2.setForeground(new Color(255, 218, 185));
//        progressBar_2.setStringPainted(true);
//        progressBar_2.setBounds(460, 101, 190, 14);
//        panel_1.add(progressBar_2);
//
//        JPanel panel_2 = new JPanel();
//        panel_2.setToolTipText("");
//        panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u6587\u4EF6\u4F20\u8F93\u7AEF",
//                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
//        panel_2.setBackground(Color.WHITE);
//        panel_2.setBounds(39, 191, 652, 202);
//        contentPane.add(panel_2);
//        panel_2.setLayout(null);
//
//        //文件传输端的进度条
//        JProgressBar progressBar_1 = new JProgressBar();
//        progressBar_1.setFont(new Font("宋体", Font.PLAIN, 18));
//        progressBar_1.setStringPainted(true);
//        progressBar_1.setForeground(new Color(240, 128, 128));
//        progressBar_1.setBackground(Color.WHITE);
//        progressBar_1.setBounds(96, 169, 472, 20);
//        panel_2.add(progressBar_1);
//
//        JLabel lblIp = new JLabel("IP\u5730\u5740:");
//        lblIp.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//        lblIp.setBounds(26, 44, 70, 27);
//        panel_2.add(lblIp);
//
//        //IP地址文本框
//        textField_1 = new JTextField();
//        textField_1.setText("127.0.0.1");
//        textField_1.setFont(new Font("新宋体", Font.PLAIN, 20));
//        textField_1.setBounds(96, 44, 184, 30);
//        panel_2.add(textField_1);
//        textField_1.setColumns(10);
//
//        JLabel label_1 = new JLabel("\u7AEF\u53E3:");
//        label_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//        label_1.setBounds(308, 42, 80, 30);
//        panel_2.add(label_1);
//
//        //端口文本框
//        textField_2 = new JTextField();
//        textField_2.setText("8080");
//        textField_2.setFont(new Font("新宋体", Font.PLAIN, 20));
//        textField_2.setColumns(10);
//        textField_2.setBounds(359, 44, 80, 30);
//        panel_2.add(textField_2);
//
//                                                   //打开本地路径的按钮
//        JButton button = new JButton("...");
//        button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser jf = new JFileChooser();
//                jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                jf.showOpenDialog(SocketFileJFrame.this);
//                textField_3.setText(jf.getSelectedFile().getPath());
//            }
//        });
//        button.setBackground(Color.WHITE);
//        button.setBounds(533, 51, 35, 20);
//        panel_2.add(button);
//
//        JLabel label_2 = new JLabel("\u8DEF\u5F84:");
//        label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
//        label_2.setBounds(489, 50, 44, 18);
//        panel_2.add(label_2);
//
//        //显示文件路径框
//        textField_3 = new JTextField();
//        textField_3.setEnabled(false);
//        textField_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//        textField_3.setBounds(96, 100, 343, 38);
//        panel_2.add(textField_3);
//        textField_3.setColumns(10);
//
//        JLabel lblNewLabel = new JLabel("\u6587\u4EF6:");
//        lblNewLabel.setFont(new Font("等线 Light", Font.PLAIN, 25));
//        lblNewLabel.setBounds(33, 97, 56, 38);
//        panel_2.add(lblNewLabel);
//
//
//                                              //确定按钮
//        JButton button_1 = new JButton("\u786E\u5B9A");
//        button_1.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                ClientFile client = new ClientFile(textField_1.getText(), Integer.parseInt(textField_2.getText()), textField_3.getText(), progressBar_1);
//                client.start();
//            }
//        });
//        button_1.setForeground(new Color(255, 255, 0));
//        button_1.setBackground(new Color(233, 150, 122));
//        button_1.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
//        button_1.setBounds(475, 100, 91, 38);
//        panel_2.add(button_1);
//    }
}
