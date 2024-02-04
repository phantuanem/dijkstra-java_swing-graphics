package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.StyleSheet.ListPainter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class Main extends JFrame {

	private JPanel panelMain, panelDraw, panelBottom;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_2;
	private JSeparator separator_1;
	private JButton btnSave;
	private JButton btnReset;
	private JLabel labelTitleSMLT;
	private JLabel lblLT;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/icon/graph.png")));
		setBackground(new Color(255, 255, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 872, 492);
		setTitle("Giải thuật tìm đường đi ngắn nhất - Niên luận cơ sở ngành KTPM");
		panelMain = new JPanel();
		panelMain.setBackground(new Color(255, 255, 255));
		panelMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelMain.setBorder(new MatteBorder(1, 0, 0, 0, new Color(166, 166, 166)));

		setContentPane(panelMain);
		panelMain.setLayout(null);
		
		panelDraw = new PanelDraw();
		panelDraw.setBackground(new Color(255, 255, 255));
		panelDraw.setBounds(0, 0, 861, 333);
		panelDraw.setBorder(new MatteBorder(2, 0, 2, 0, new Color(41, 69, 115)));
		((PanelDraw) panelDraw).setFrameMain(this);
		panelMain.add(panelDraw);
		
		panelBottom = new JPanel();
		panelBottom.setBackground(new Color(255, 255, 255));
		panelBottom.setBounds(0, 332, 861, 125);
		panelMain.add(panelBottom);
		panelBottom.setLayout(null);
		
		lblNewLabel = new JLabel("Đường đi:");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 5, 80, 20);
		panelBottom.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tổng chi phí:");
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 25, 92, 20);
		panelBottom.add(lblNewLabel_1);
		
		JLabel lblPath = new JLabel("");
		lblPath.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		lblPath.setBounds(80, 5, 835, 20);
		panelBottom.add(lblPath);
		
		JLabel lblCost = new JLabel("");
		lblCost.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		lblCost.setBounds(100, 24, 144, 20);
		panelBottom.add(lblCost);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 64, 160, 2);
		panelBottom.add(separator);
		
		lblNewLabel_2 = new JLabel("© Mỹ Thu");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(10, 84, 64, 14);
		panelBottom.add(lblNewLabel_2);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(10, 98, 51, 2);
		panelBottom.add(separator_1);
		
		JButton btnInfo = new JButton("Hướng dẫn sử dụng");
		btnInfo.addActionListener(e->{
			this.showFormInfo();
		});
		btnInfo.setFont(new Font("Arial", Font.PLAIN, 13));
		btnInfo.setBounds(570, 77, 191, 29);
		btnInfo.setFocusable(false);
		btnInfo.setRolloverEnabled(false);
		btnInfo.setBackground(Color.white);
		btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelBottom.add(btnInfo);
		
		btnSave = new JButton("Lưu");
		btnSave.addActionListener(e->{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to save"); 
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.graph", "graph"));
			 
			int userSelection = fileChooser.showSaveDialog(this);
			 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    File fileToSave = fileChooser.getSelectedFile();
			    ((PanelDraw) panelDraw).handleSave(fileToSave.getAbsolutePath());
			}
		});
		btnSave.setFont(new Font("Arial", Font.PLAIN, 13));
		btnSave.setBounds(271, 76, 73, 30);
		btnSave.setFocusable(false);
		btnSave.setRolloverEnabled(false);
		btnSave.setBackground(Color.white);
		btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelBottom.add(btnSave);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(e->{
			((PanelDraw) panelDraw).clear();
		});
		btnReset.setFont(new Font("Arial", Font.PLAIN, 13));
		btnReset.setBounds(162, 76, 82, 30);
		btnReset.setFocusable(false);
		btnReset.setRolloverEnabled(false);
		btnReset.setBackground(Color.white);
		btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelBottom.add(btnReset);
		
		JButton btnUpdate = new JButton("Mở");
		btnUpdate.addActionListener(e->{
			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.graph", "graph"));
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (file == null) {
                    return;
                }
                ((PanelDraw) panelDraw).handleOpenFileGtaph(chooser.getSelectedFile().getAbsolutePath());

            }
		});
		btnUpdate.setFont(new Font("Arial", Font.PLAIN, 13));
		btnUpdate.setBounds(374, 77, 90, 28);
		btnUpdate.setFocusable(false);
		btnUpdate.setRolloverEnabled(false);
		btnUpdate.setBackground(Color.white);
		btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelBottom.add(btnUpdate);
		
		JButton btnClose = new JButton("Đóng");
		btnClose.addActionListener(e->{
			this.dispose();
		});
		btnClose.setBounds(771, 77, 80, 29);
		btnClose.setFont(new Font("Arial", Font.PLAIN, 13));
		btnClose.setFocusable(false);
		btnClose.setRolloverEnabled(false);
		btnClose.setBackground(Color.white);
		btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelBottom.add(btnClose);
		
		labelTitleSMLT = new JLabel("Số miền liên thông:");
		labelTitleSMLT.setForeground(Color.BLACK);
		labelTitleSMLT.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		labelTitleSMLT.setBounds(10, 45, 135, 20);
		panelBottom.add(labelTitleSMLT);
		
		lblLT = new JLabel("");
		lblLT.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		lblLT.setBounds(138, 45, 144, 20);
		panelBottom.add(lblLT);
		
		((PanelDraw) panelDraw).setLblPathAndCost(lblPath, lblCost,lblLT);
		
	}
	
	
	public void showFormInfo() {
		JDialog formInfo = new JDialog(this,true);
		
		formInfo.setTitle("Hướng dẫn sử dụng phần mềm");
		formInfo.setBounds(getX() + 150, getY() + 50, 606, 390);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 0, 590, 340);
		panel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(166, 166, 166)));
		formInfo.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("NIÊN LUẬN CƠ SỞ NGÀNH KTPM");
		lblNewLabel.setForeground(new Color(12, 19, 71));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 21, 570, 27);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Giải Thuật Tìm Đường Đi Ngắn Nhất");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 17));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 52, 570, 21);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Hướng dẫn sử dụng phần mềm:");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(10, 118, 258, 27);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Ctrl + nhấn giữ chuột: trái:");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(10, 156, 230, 21);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Shift + click chuột trái:");
		lblNewLabel_4.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(10, 188, 230, 27);
		panel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Di chuyển vị trí nút");
		lblNewLabel_5.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_5.setBounds(250, 156, 258, 14);
		panel.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Xóa nút");
		lblNewLabel_6.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_6.setBounds(250, 194, 107, 14);
		panel.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Click cuột trái hai lần nhãn cung:");
		lblNewLabel_7.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_7.setBounds(10, 226, 230, 14);
		panel.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Cập nhật chi phí cung");
		lblNewLabel_8.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_8.setBounds(250, 226, 181, 14);
		panel.add(lblNewLabel_8);
		
		JLabel lblNewLabel_9 = new JLabel("Alt + click chuột trái nhãn cung:");
		lblNewLabel_9.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_9.setBounds(10, 260, 230, 14);
		panel.add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("Xóa cung");
		lblNewLabel_10.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_10.setBounds(250, 260, 107, 14);
		panel.add(lblNewLabel_10);
		
		JLabel lblNewLabel_11 = new JLabel("Sinh viên thực hiện:");
		lblNewLabel_11.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_11.setBounds(10, 93, 145, 14);
		panel.add(lblNewLabel_11);
		
		JLabel lblNewLabel_12 = new JLabel("Mỹ Thu");
		lblNewLabel_12.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_12.setBounds(165, 93, 153, 14);
		panel.add(lblNewLabel_12);
		
		JLabel lblNewLabel_9_1 = new JLabel("Ctrl + Shift + click chuột trái:");
		lblNewLabel_9_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_9_1.setBounds(10, 289, 230, 14);
		panel.add(lblNewLabel_9_1);
		
		JLabel lblNewLabel_10_1 = new JLabel("Chọn điểm đầu");
		lblNewLabel_10_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_10_1.setBounds(250, 289, 107, 14);
		panel.add(lblNewLabel_10_1);
		
		JLabel lblNewLabel_9_1_1 = new JLabel("Ctrl + Alt + click chuột trái:");
		lblNewLabel_9_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_9_1_1.setBounds(10, 315, 230, 14);
		panel.add(lblNewLabel_9_1_1);
		
		JLabel lblNewLabel_10_1_1 = new JLabel("Chọn điểm cuối");
		lblNewLabel_10_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_10_1_1.setBounds(250, 315, 125, 14);
		panel.add(lblNewLabel_10_1_1);
		
		formInfo.setVisible(true);
	}
}
