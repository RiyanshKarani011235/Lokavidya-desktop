package Dialogs;

import gui.Call;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VideoFormat extends JFrame {

	private JPanel contentPane;
	private JTextField txtWidth;
	private JTextField txtHeight;
	private JLabel lblFramerate;
	private JTextField txtFramerate;
	private JButton buttonApply;
	private JButton btnCancel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VideoFormat frame = new VideoFormat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static boolean validateIsInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean validateIntegerInRange(int num, int min, int max) {
		if (num < min || num > max) {
			return false;
		} return true;
	}
	
	public static boolean validateIntegerEntryInTextField(String s, int min, int max) {
		if(!validateIsInteger(s)) {
			return false;
		}
		return validateIntegerInRange(Integer.parseInt(s), min, max);
	}

	/**
	 * Create the frame.
	 */
	public VideoFormat() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 440, 269);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblDecideVideoFormat = new JLabel("Video Format");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDecideVideoFormat, 34, SpringLayout.WEST, contentPane);
		lblDecideVideoFormat.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(lblDecideVideoFormat);
		
		JLabel lblScreenResolution = new JLabel("Screen Resolution: ");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblScreenResolution, 97, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblScreenResolution, 34, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDecideVideoFormat, -34, SpringLayout.NORTH, lblScreenResolution);
		lblScreenResolution.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblScreenResolution);
		
		txtWidth = new JTextField();
		txtWidth.setText(Integer.toString(Call.workspace.videoWidth));
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtWidth, 0, SpringLayout.NORTH, lblScreenResolution);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtWidth, 13, SpringLayout.EAST, lblScreenResolution);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtWidth, -199, SpringLayout.EAST, contentPane);
		txtWidth.setColumns(10);
		contentPane.add(txtWidth);
		
		JLabel lblX = new JLabel("X");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblX, 0, SpringLayout.NORTH, lblScreenResolution);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblX, 1, SpringLayout.EAST, txtWidth);
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblX);
		
		txtHeight = new JTextField(Integer.toString(Call.workspace.videoHeight));
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtHeight, 0, SpringLayout.NORTH, lblScreenResolution);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtHeight, 6, SpringLayout.EAST, lblX);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtHeight, -129, SpringLayout.EAST, contentPane);
		txtHeight.setColumns(10);
		contentPane.add(txtHeight);
		
		lblFramerate = new JLabel("Framerate:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFramerate, 26, SpringLayout.SOUTH, lblScreenResolution);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFramerate, 34, SpringLayout.WEST, contentPane);
		lblFramerate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblFramerate);
		
		txtFramerate = new JTextField();
		txtFramerate.setText(Integer.toString(Call.workspace.framerate));
		sl_contentPane.putConstraint(SpringLayout.WEST, txtFramerate, -40, SpringLayout.EAST, lblScreenResolution);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, txtFramerate, 0, SpringLayout.SOUTH, lblFramerate);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtFramerate, 0, SpringLayout.EAST, lblScreenResolution);
		txtFramerate.setColumns(10);
		contentPane.add(txtFramerate);
		
		
		
		buttonApply = new JButton("OK");
		buttonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!validateIntegerEntryInTextField(txtWidth.getText(), 0, 1000000000)) {
					JOptionPane.showMessageDialog(null, "Width should be a positive integer", "", JOptionPane.INFORMATION_MESSAGE);
				} else if(!validateIntegerEntryInTextField(txtHeight.getText(), 0, 1000000000)) {
					JOptionPane.showMessageDialog(null, "Height should be a positive integer", "", JOptionPane.INFORMATION_MESSAGE);
				} else if(!validateIntegerEntryInTextField(txtFramerate.getText(), 3, 30)) {
					JOptionPane.showMessageDialog(null, "frame rate should be an integer between 3 and 30", "", JOptionPane.INFORMATION_MESSAGE);
				} else {
					int width=Integer.parseInt(txtWidth.getText());
					int height=Integer.parseInt(txtHeight.getText());
					int frameRate=Integer.parseInt(txtFramerate.getText());
					Call.workspace.videoHeight=height;
					Call.workspace.videoWidth=width;
					Call.workspace.framerate=frameRate;
					dispose();
				}
			}
		});
		buttonApply.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(buttonApply);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, buttonApply, 0, SpringLayout.NORTH, btnCancel);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonApply, -6, SpringLayout.WEST, btnCancel);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnCancel, -24, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnCancel, 0, SpringLayout.EAST, txtHeight);
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnCancel);
	}
}