package Dialogs;

import gui.Call;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import Dialogs.CreateProject.ProgressDialog.Task;

import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.utils.GeneralUtils;



public class OpenPresentation {
	public JFrame frame;
	public String pathDef;
	public String path;
	private JButton btnNewButton_1;
	private JTextField textField_2;
	
	public JProgressBar progressBar;
	public JPanel innerPanel;
	private JButton button;
	private JLabel lblNewLabel1;
	
	class ProgressDialog extends JPanel
	implements ActionListener, 
	PropertyChangeListener{
	public Task task;
	
	class Task extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			int progress=0;
			 setProgress(0);
			 System.out.println("calling creation");
			// CreateProject.projectCreationMethod();	
			 try {
				 	Call.workspace.startOperation();
				 	setProgress(10);
				 	int displayIndex=Call.workspace.presentationInnerPanel.getComponentCount();
				 	ProjectService.importPresentation(path, Call.workspace.currentProject,OpenPresentation.this);
				 	System.out.println("Returning here");
				 	setProgress(75);
				 	
				 	if (!Call.workspace.cancelled) {
						Call.workspace.repopulateProject();
						
						Call.workspace.revalidate();
						Call.workspace.repaint();
						Call.workspace.endOperation();
						setProgress(100);
						Thread.sleep(1000);
						frame.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						frame.dispose();
						
					}
				 	else
				 	{
				 		lblNewLabel1.setText("Cancelling import");
				 		Call.workspace.cancelOperation();
				 		setProgress(50);
				 		Thread.sleep(1000);
						frame.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						frame.dispose();
				 	}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			
			return null;
		}
		 
	 }
	 
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setIndeterminate(false);
            progressBar.setValue(progress);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	ProgressDialog() {

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JCheckBox checkbox = new JCheckBox("Don't show again");
		String message = "The presentation will be appended to the end of the project.";
		Object[] parameters = {message, checkbox};
        JOptionPane.showMessageDialog(this, parameters);
        
        // check if checkbox clicked
        if(checkbox.isSelected()) {
        	// selected, disable this message in the future
        } else {
        	// not selected
        }
        
        innerPanel.setVisible(true);
        System.out.println("Yes option");
        task = new Task();
        task.addPropertyChangeListener(this);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task.execute();
	}
}
	
	

	
	public void setprogressvalue(int value){
		progressBar.setValue(value);
	}
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Call.workspace.cancelled=false;
					System.out.println("isnide");
					OpenPresentation window = new OpenPresentation();
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
	public OpenPresentation() {
		initialize();
	}
	
	public void initialize() {
		
		        
        System.out.println("passed 1");
		frame = new JFrame();
		frame.setBounds(100, 100, 502, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		JLabel lblNewLabel = new JLabel("Open Presentation");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel);
		
		
		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); 
        progressBar.setIndeterminate(true);
      
        innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.add(progressBar);
        innerPanel.setSize(400, 30);
        innerPanel.setVisible(false);
        innerPanel.setOpaque(true);
        
        
        lblNewLabel1 = new JLabel("Importing presentation. Please wait....");
        innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
        //innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);
        
        textField_2 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, -3, SpringLayout.NORTH, textField_2);
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, -6, SpringLayout.WEST, textField_2);
		springLayout.putConstraint(SpringLayout.NORTH, textField_2, 73, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField_2, 197, SpringLayout.WEST, frame.getContentPane());

		textField_2.setColumns(10);
		String Os = System.getProperty("os.name");
		pathDef=GeneralUtils.getDocumentsPath();
		textField_2.setText(pathDef);
		frame.getContentPane().add(textField_2);
		
		
		
		JButton btnNewButton_2 = new JButton(" ... ");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_2, 74, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField_2, -3, SpringLayout.WEST, btnNewButton_2);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_2, -10, SpringLayout.EAST, frame.getContentPane());
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path=new DirectoryChooser(pathDef,"ppt").selectedfile;

				textField_2.setText(path);
			}
		});
		frame.getContentPane().add(btnNewButton_2);
		
		
		btnNewButton_1 = new JButton("Import");
		springLayout.putConstraint(SpringLayout.WEST, innerPanel, 38, SpringLayout.EAST, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.SOUTH, innerPanel, 0, SpringLayout.SOUTH, btnNewButton_1);
		
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 27, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -28, SpringLayout.SOUTH, frame.getContentPane());
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_2.getText().equals(""))
				{
					System.out.println("Path null");
					JOptionPane.showMessageDialog(null, "Enter the presentation location", "", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					path=textField_2.getText();
					System.out.println(path);		
					
					// check if valid path
					if(path == "" || new File(path).isDirectory()) {
						JOptionPane.showMessageDialog(null, "Enter the presentation location", "", JOptionPane.INFORMATION_MESSAGE);
					} else {
						//Where the GUI is constructed:
						new ProgressDialog();
						//Call.workspace.repopulateProject();
						System.out.println("setup done");						
						//frame.dispose();
					}

				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		frame.getContentPane().add(btnNewButton_1);
		
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Call.workspace.cancelled=true;
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, button, 0, SpringLayout.NORTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.EAST, button, -25, SpringLayout.EAST, frame.getContentPane());
		button.setFont(new Font("Tahoma", Font.PLAIN, 14));
		// disable cancel button - ironstein 22-11-16
//		frame.getContentPane().add(button);
		
	}
	}
	

