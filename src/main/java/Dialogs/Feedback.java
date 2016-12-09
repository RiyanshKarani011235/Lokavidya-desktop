package Dialogs;



import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;





public class Feedback {
	public JFrame frame;
	public String pathDef;
	public String path;
	private JButton btnNewButton_1;
	private JTextField textField_2;
	
	public JPanel innerPanel;
	private JTextField textField;
	private JLabel lblNumber;
	private JTextField textField_1;
	private JLabel lblEmailId;
	private String email, phonenumber, comment, name; 
	private JLabel lblComments;
	private JTextArea textArea;
	private JTextArea textArea_1;
	//private JTextArea textArea_2;
	private JScrollPane scrollPane;
	private JTextArea  textArea_3;

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("inside");
					Feedback window = new Feedback();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static void copyFile( File from, File to ){
	    try {
			Files.copy( from.toPath(), to.toPath() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Feedback() {
		try {
		initialize();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initialize()  {
		
		        
        System.out.println("passed 1");
		frame = new JFrame();
		frame.setBounds(100, 100, 502, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel);
		
      
        innerPanel = new JPanel();
        springLayout.putConstraint(SpringLayout.WEST, innerPanel, 153, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, innerPanel, -28, SpringLayout.SOUTH, frame.getContentPane());
        innerPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.setSize(400, 30);
        innerPanel.setVisible(false);
        innerPanel.setOpaque(true);
        //innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);
        
        textField_2 = new JTextField();
        springLayout.putConstraint(SpringLayout.WEST, textField_2, 194, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, textField_2, -201, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, textField_2, -91, SpringLayout.EAST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 1, SpringLayout.NORTH, textField_2);
        springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, -19, SpringLayout.WEST, textField_2);

		textField_2.setColumns(10);
		String Os = System.getProperty("os.name");
		if (Os.startsWith("Windows")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "My Documents";
		}
		else if (Os.startsWith("Linux")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "Documents";
		}
		
		else if (Os.startsWith("Mac")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "Documents";
		}
		//textField_2.setText(pathDef);
		frame.getContentPane().add(textField_2);
		
		
		btnNewButton_1 = new JButton("Submit");
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_1, -29, SpringLayout.WEST, innerPanel);
		
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		frame.getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 6, SpringLayout.SOUTH, textField_2);
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, textField_2);
		springLayout.putConstraint(SpringLayout.EAST, textField, -91, SpringLayout.EAST, frame.getContentPane());
		textField.setText((String) null);
		textField.setColumns(10);
		frame.getContentPane().add(textField);
		
		lblNumber = new JLabel("Phone Number");
		springLayout.putConstraint(SpringLayout.NORTH, lblNumber, 1, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, lblNumber, 58, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNumber, 0, SpringLayout.EAST, lblNewLabel);
		lblNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNumber);
		
		textField_1 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, 6, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, textField_2);
		springLayout.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, textField_2);
		textField_1.setText((String) null);
		textField_1.setColumns(10);
		frame.getContentPane().add(textField_1);
		
		lblEmailId = new JLabel("Email ID");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmailId, 1, SpringLayout.NORTH, textField_1);
		springLayout.putConstraint(SpringLayout.EAST, lblEmailId, -19, SpringLayout.WEST, textField_1);
		lblEmailId.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblEmailId);
		
		lblComments = new JLabel("Comments");
		springLayout.putConstraint(SpringLayout.NORTH, lblComments, 22, SpringLayout.SOUTH, lblEmailId);
		springLayout.putConstraint(SpringLayout.EAST, lblComments, 0, SpringLayout.EAST, lblNewLabel);
		lblComments.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblComments);
		
		scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 17, SpringLayout.SOUTH, textField_1);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 19, SpringLayout.EAST, lblComments);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 80, SpringLayout.SOUTH, textField_1);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 236, SpringLayout.EAST, lblComments);
		frame.getContentPane().add(scrollPane);
		
		textArea_3 = new JTextArea();
		textArea_3.setLineWrap(true);
		textArea_3.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea_3);


		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_2.getText().equals("") || textField_1.getText().equals("") || textField.getText().equals("") || textArea_3.getText().equals(""))
				{
					//TODO Update... 
					System.out.println("Path null");
					JOptionPane.showMessageDialog(null, "All fields are mandatory.", "Missing data", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					email = textField_1.getText(); 
					name =textField_2.getText();
					phonenumber = textField.getText();
					comment = textArea_3.getText();
					
					System.out.println(email+ " "+ name+ " "+phonenumber + " "+comment);
		
					OkHttpClient client = new OkHttpClient();
					
					    RequestBody formBody = new FormBody.Builder()
					        .add("entry.84497324", name)
					        .add("entry.785499799",phonenumber)
					        .add("entry.181352694", email)
					        .add("entry.1838113120", comment)
					        .build();
					    Request request = new Request.Builder()
					        .url("https://docs.google.com/forms/d/1nuLhkiwIaoaQK5ME3m-GEpUir9nmEoDHuS7Zk67E6e8/formResponse")
					        .post(formBody)
					        .build();

					    Response response = null;
						try {
							response = client.newCall(request).execute();
						} catch (IOException e2) {
							JOptionPane.showMessageDialog(null, "Feedback submission failed. Please check your net connection", "Feedback Submission Failed", JOptionPane.INFORMATION_MESSAGE);
							e2.printStackTrace();
						}
					    if (!response.isSuccessful())
					    {	
					    	 JOptionPane.showMessageDialog(null, "Feedback submission failed. Please check your net connection", "Feedback Submission Failed", JOptionPane.INFORMATION_MESSAGE);
					    	try {
								throw new IOException("Unexpected code " + response);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					    }
					    JOptionPane.showMessageDialog(null, "Thank you for your feedback!", "Feedback Submitted", JOptionPane.INFORMATION_MESSAGE);
				        textField_1.setText("");
				        textField_2.setText("");
				        textField.setText("");
				        textArea_3.setText("");
				}
				}
				});
		
	}
	}
	

