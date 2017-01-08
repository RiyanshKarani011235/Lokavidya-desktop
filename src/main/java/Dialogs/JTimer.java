package Dialogs;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.Call;
import gui.Workspace;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.Timer;

public class JTimer extends JFrame {
	
	private JPanel contentPane;
	private JLabel label;
	private int cntr;
	private static JTimer frame;
	static Timer t;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {			
					frame = new JTimer();	
					t.start();
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
	public JTimer() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 340, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// center
		setLocationRelativeTo(null);
		// remove the cancel option
		setUndecorated(true);
		
		label = new JLabel("3");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 60));
		contentPane.add(label, BorderLayout.CENTER);
		
		JLabel lblRecordingStartsIn = new JLabel("Recording Starts in:");
		lblRecordingStartsIn.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRecordingStartsIn.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblRecordingStartsIn, BorderLayout.NORTH);
		
		cntr=2;
		t = new Timer(1000 * 1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // do your reoccuring task
            	if(cntr==0){
            		frame.setVisible(false);
            		
					// enable screen recording button that was disabled during countdown
            		try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Call.workspace.btnScreenRec.setEnabled(true);
            		Call.workspace.startScreenRecording();
            		frame.dispose();
            	}
            	label.setText(Integer.toString(cntr));
            	cntr--;
            }
		});
	}

}
