package Dialogs;

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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;

import org.apache.commons.io.FilenameUtils;

import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Segment.SegmentType;
import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.utils.*;

import Dialogs.OpenVideo.ProgressDialog;
import Dialogs.OpenVideo.ProgressDialog.Task;
import gui.Call;

public class OpenAndroidExp {
	public JFrame frame;
	public String pathDef;
	public String path;
	private JButton btnNewButton_1;
	private JTextField textField_2;
	private JButton btnCancel;

	public ProgressDialog  dialog;
	private JPanel innerPanel;
	private JProgressBar progressBar;
	private JLabel lblNewLabel1;
	
	File projectTmp;
	
	class ProgressDialog extends JPanel
	implements ActionListener, 
	PropertyChangeListener{
		
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		 public Task task;
		
		 
		 class Task extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
			
				path=textField_2.getText();
				
				
				ProjectService.exportAndroidProject(Call.workspace.currentProject.getProjectURL(), path);
				//System.out.println(projName);
				System.out.println(path);
				setProgress(100);
				Thread.sleep(1000);
				frame.dispose();
				JOptionPane.showMessageDialog(null, "project has been successfully exported as : \n" + projectTmp, "", JOptionPane.INFORMATION_MESSAGE);
				return null;
			 
		 }
		 


		public void propertyChange(PropertyChangeEvent evt) {
			
			if ("progress" == evt.getPropertyName()) {
	            int progress = (Integer) evt.getNewValue();
	            progressBar.setIndeterminate(false);
	            progressBar.setValue(progress);
			}
			
		}


		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

		 }
		ProgressDialog() {

			innerPanel.setVisible(true);
	        System.out.println("Progress dialog created");
	        task = new Task();
	        task.addPropertyChangeListener(this);
	        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	        task.execute();
		}
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
}
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpenAndroidExp window = new OpenAndroidExp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public OpenAndroidExp() {
		initialize();
	}
	
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 530, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		JLabel lblNewLabel = new JLabel("Export destination ");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 70, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 21, SpringLayout.WEST, frame.getContentPane());
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel);
		
		textField_2 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_2, 3, SpringLayout.NORTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, textField_2, 6, SpringLayout.EAST, lblNewLabel);
		springLayout.putConstraint(SpringLayout.EAST, textField_2, -105, SpringLayout.EAST, frame.getContentPane());

		textField_2.setColumns(10);
		String Os = System.getProperty("os.name");
		pathDef=GeneralUtils.getDocumentsPath();
		textField_2.setText(pathDef);
		frame.getContentPane().add(textField_2);
		
		JButton btnNewButton_2 = new JButton(" ... ");
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_2, 6, SpringLayout.EAST, textField_2);
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_2, 0, SpringLayout.SOUTH, lblNewLabel);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path=new DirectoryChooser(pathDef,"folder").selectedfile;

				textField_2.setText(path);
			}
		});
		frame.getContentPane().add(btnNewButton_2);
		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); 
        progressBar.setIndeterminate(true);
		innerPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, innerPanel, 0, SpringLayout.EAST, btnNewButton_2);
			
        innerPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.add(progressBar);
        innerPanel.setSize(400, 30);
        innerPanel.setVisible(false);
        innerPanel.setOpaque(true);
        
        
        lblNewLabel1 = new JLabel("Exporting..Please wait.");
        innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
        //innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);
		
		btnNewButton_1 = new JButton("Export");
		springLayout.putConstraint(SpringLayout.SOUTH, innerPanel, 0, SpringLayout.SOUTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 27, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -28, SpringLayout.SOUTH, frame.getContentPane());
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_2.getText().equals(""))
				{
					System.out.println("Path null");
					JOptionPane.showMessageDialog(null, "Enter the export location", "", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					int flag=0;
					Project project = ProjectService.getInstance(Call.workspace.currentProject.getProjectURL()+File.separator+FilenameUtils.getName(Call.workspace.currentProject.getProjectURL())+".json"); 
					//String tmpPath=System.getProperty("java.io.tmpdir");
//					File projectTmp = new File(path,project.getProjectName());
					projectTmp = new File(textField_2.getText(), FilenameUtils.getName(Call.workspace.currentProject.getProjectURL()+".zip"));
					System.out.println("projectTmp : " + projectTmp.getAbsolutePath());
					if(projectTmp.exists()){
						flag = JOptionPane.showOptionDialog(null,"File Already Exists. Do you want to replace?","Warning",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null, new String[]{"Replace","Cancel"},null);
						System.out.println(flag);
					}
					if(flag==0){
						dialog=new ProgressDialog();
					}
					
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		frame.getContentPane().add(btnNewButton_1);
		
		btnCancel = new JButton("Close");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Call.workspace.cancelled=true;
				frame.dispose();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.WEST, btnCancel, 17, SpringLayout.EAST, btnNewButton_1);
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(btnCancel);
		
	}

}
