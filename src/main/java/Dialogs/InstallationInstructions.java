package Dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import Dialogs.OpenProject.ProgressDialog;
import gui.Call;

public class InstallationInstructions {
	
	JFrame frame;
	String pathDef;
	JPanel contentPane;
	
	public InstallationInstructions() {
		init();
	}
	
	public void init() {
		
		String osPathString;
		String osname = System.getProperty("os.name");

		if (osname.contains("Windows")) {
			osPathString = "windows";
		} else if (osname.toLowerCase().contains("mac")){
			osPathString = "mac";
		} else {
			osPathString = "linux";
		}
		
		String installationFilePath = new File("resources", "install_" + osPathString + ".txt").getAbsolutePath();
		System.out.println("installationFile path : " + installationFilePath);
		
		String instructions = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(installationFilePath));
			StringBuilder sb = new StringBuilder();
		    String line = reader.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = reader.readLine();
		    }
		    instructions = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] instructionsList = instructions.split("\n");

		JFrame frame = new JFrame();
		
        // build JTextArea
        JTextArea textArea = new JTextArea("INSTALLATION INSTRUCTIONS : \n\n");
        for(int i=0; i<instructionsList.length; i++) {
    	    textArea.append(instructionsList[i] + "\n\n");
    	    textArea.setWrapStyleWord(true);
    	    textArea.setLineWrap(true);
    	    textArea.setOpaque(false);
    	    textArea.setEditable(false);
    	    textArea.setFocusable(false);
    	    textArea.setBackground(UIManager.getColor("Label.background"));
    	    textArea.setFont(UIManager.getFont("Label.font"));
    	    textArea.setBorder(UIManager.getBorder("Label.border"));
		}
        
        textArea.append("for viewing installation instructions anytime, click help>installation instructions\n\n");
        
        // add padding to textArea
        Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        textArea.setBorder(paddingBorder);
        textArea.setPreferredSize(new Dimension(400, 400));
        
        // top JPanel for text
        JPanel top = new JPanel();
        top.add(textArea, BorderLayout.CENTER);
        top.setPreferredSize(new Dimension(400, 400));
        
        // okButton
        JButton okButton = new JButton("OK");
        okButton.setSize(new Dimension(40, 40));
        okButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					FileOutputStream out = new FileOutputStream(new File("lib", "installationRead.txt").getAbsolutePath());
					byte[] data = {};
					out.write(data);
					out.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.dispose();
        	}
        });
        
        // bottom JPanel for button
        JPanel bottom = new JPanel();
        bottom.add(okButton);
        
        // add frame and calculate size
        frame.setBackground( new Color(0,0,0));
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE);
        frame.add(top, BorderLayout.NORTH);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setResizable(false);
        
        // center the frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2);
        
        // make frame visible
        frame.setVisible(true);		
	}
	
	public static void main(String[] args) {
		new InstallationInstructions();
	}
	
}
