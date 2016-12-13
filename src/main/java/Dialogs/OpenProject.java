package Dialogs;

import gui.Call;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import Dialogs.CreateProject.ProgressDialog;
import Dialogs.CreateProject.ProgressDialog.Task;

import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.utils.*;




public class OpenProject {

	private JFrame frame;
	public String path;
	private String pathDef;
	private int count = 0;
	private int projectCount = 0;
	private JFileChooser chooser;
	private JTextField textField;
	private JButton btnNewButton;
	public ProgressDialog  dialog;
	private int index;
	private int cnt;
	private JLabel lblNewLabel_2;
	private JTextField textField_1;
	private JButton btnNewButton_1,btnNewButton_2,btnNewButton_3;
	private JRadioButton [] radioButton = new JRadioButton[30];
	private JLabel lblNewLabel_3;
	private JScrollPane scrollPane;
	private JPanel panel;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private FileFilter directoryFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};
	private JPanel innerPanel;
	private JProgressBar progressBar;
	private JLabel lblNewLabel1;
	
	class ProgressDialog extends JPanel
	implements ActionListener, 
	PropertyChangeListener{
		private static final long serialVersionUID = 1L;
		public Task task;
		
		class Task extends SwingWorker<Void, Void> {
			@Override
			protected Void doInBackground() throws Exception {
				setProgress(0);
				progressBar.setValue(0);
				String fileName = radioButton[index].getText();
				Call.workspace.name=fileName;
				System.out.println(fileName);
				//Project p = new Project(fileName, path);
				//ProjectCommunicationInstance.launchInstance(p);
				
				// if filename is entered
				if(path.endsWith(fileName)) {
					path = new File(path).getParentFile().getAbsolutePath();
				}
				Call.workspace.location = path;
				Call.workspace.path = new File(Call.workspace.location, Call.workspace.name).getAbsolutePath();
				File jsonFile = new File(FilenameUtils.concat(Call.workspace.path, fileName+".json"));
				System.out.println("Use json: "+jsonFile.getAbsolutePath());
				Call.workspace.currentProject = ProjectService.getInstance(jsonFile.getAbsolutePath());
				
				if(Call.workspace.currentProject == null) {
					// corrupt project
					frame.dispose();
					JOptionPane.showMessageDialog(null, "The project you were trying to import was corrputed. \nPlease create a new project or import another project.");
					return null;
				}
				
				setProgress(30);
				progressBar.setValue(30);
				if(Call.workspace.cancelled) {
					lblNewLabel1.setText("Cancelling");
					setProgress(50);
					progressBar.setValue(50);
					Thread.sleep(1000);
					frame.dispose();
				} else {
					btnNewButton_3.setEnabled(false);
					System.out.println("Loading project");
					try {
						Call.workspace.enable();
						setProgress(40);
						progressBar.setValue(40);
						Call.workspace.initializeStates();
						setProgress(50);
						progressBar.setValue(50);
						Call.workspace.repopulateProject();
						setProgress(100);
						progressBar.setValue(100);
						Thread.sleep(1000);
						frame.dispose();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				return null;
			 
		 }
		 


		public void propertyChange(PropertyChangeEvent evt) {
			 System.out.println("Test :"+evt.getPropertyName());
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
	
    public boolean findProjectFolder( String dirName){
    	
    	// avoid null pointer exception (Bug 4)
    	if(dirName == null) {
    		return false;
    	}
    	
    	System.out.println("findProjectFolder : dirname : " + dirName);
    	File dir = new File(dirName);

    	File[] listFiles= dir.listFiles(new FilenameFilter() { 
    		public boolean accept(File dir, String filename) { 
    			return filename.endsWith(".json"); 
    		}
    	});
    	if (listFiles != null) {
    		System.out.println("not null");
	    	if(listFiles.length>0) {
	    		return true;
	    	}
    	}
    	return false;
    }
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Call.workspace.cancelled=false;
					OpenProject window = new OpenProject();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OpenProject() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 528, 299);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		pathDef=GeneralUtils.getDocumentsPath();
		JLabel lblNewLabel = new JLabel("Open Project");
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 24, SpringLayout.WEST, frame.getContentPane());
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Project Location");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 95, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, -28, SpringLayout.NORTH, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_1, 0, SpringLayout.WEST, lblNewLabel);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 0, SpringLayout.NORTH, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.WEST, textField, 12, SpringLayout.EAST, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.EAST, textField, -182, SpringLayout.EAST, frame.getContentPane());
		textField.setText(pathDef);
		
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		btnNewButton = new JButton("Browse..");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, -1, SpringLayout.NORTH, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 6, SpringLayout.EAST, textField);
		
		final String textFieldInitialText = textField.getText();
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path=new DirectoryChooser(pathDef,"").selectedfile;
				//System.out.println(path);
				
				// reset the textField value to initial value (Bug 5)
				System.out.println(path);
				if(path == null) {
					path = textFieldInitialText;
				}
				textField.setText(path);
				
				if(findProjectFolder(path)){
					count =0;
					projectCount=0;
					panel.removeAll();
					String[] subDirs = path.split(Pattern.quote(File.separator));
					String fileName=subDirs[subDirs.length-1];
					JViewport port = scrollPane.getViewport();
					radioButton[count] = new JRadioButton();

					radioButton[count].setText(fileName);

					buttonGroup.add(radioButton[count]);
					panel.add(radioButton[count]);
					btnNewButton_2.setEnabled(true);
					++count;
					projectCount++;
					panel.revalidate();
					port.revalidate();
				}
				else
				{
					count =0;
					projectCount=0;
					panel.removeAll();
					JViewport port = scrollPane.getViewport();
					File outer=new File(path);
					File[] subdirs=outer.listFiles(directoryFilter);
					for(int i=0;i<subdirs.length;i++)
					{
						if(findProjectFolder(subdirs[i].getAbsolutePath()))
						{
							String fileName=subdirs[i].getName();

							System.out.println(fileName);
	
							radioButton[count] = new JRadioButton();
							radioButton[count].setText(fileName);
							buttonGroup.add(radioButton[count]);
							panel.add(radioButton[count]);
							++count;
							
							btnNewButton_2.setEnabled(true);
							projectCount++;
						}
					}
					System.out.println("count: "+count);
					panel.revalidate();
					port.revalidate();
				}
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		btnNewButton_2 = new JButton("Open");
		
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				index=0;
				cnt=0;
				for(int i=0;i<projectCount;i++)
				{
					if(radioButton[i].isSelected()==true)
					{
						index=i;
						cnt++;
					}
				}
				if (cnt>1) {
					JOptionPane.showMessageDialog(null, "Select only one project", "", JOptionPane.INFORMATION_MESSAGE);
				} else if(cnt==0) {
					JOptionPane.showMessageDialog(null, "Select a project", "", JOptionPane.INFORMATION_MESSAGE);
				} else {
					btnNewButton_3.setText("Cancel");
					dialog = new ProgressDialog();
				}
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(btnNewButton_2);
		// Cancel Button 
		btnNewButton_3 = new JButton("Close");
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_3, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_3, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_2, 0, SpringLayout.NORTH, btnNewButton_3);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_2, -6, SpringLayout.WEST, btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_3.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	if(btnNewButton_3.getText().equals("Cancel"))
		    		Call.workspace.cancelled=true;
		    	else
		    		frame.dispose();
		    }
		});
		frame.getContentPane().add(btnNewButton_3);
		/*
		for(int i=0;i<30;i++)
		{	
			try{
			chckbxNewCheckBox[i] = new JCheckBox("");
			chckbxNewCheckBox[i].setVisible(false);
			chckbxNewCheckBox[i].setSelected(false);
			springLayout.putConstraint(SpringLayout.NORTH, chckbxNewCheckBox[0], 24, SpringLayout.SOUTH, textField);
			springLayout.putConstraint(SpringLayout.WEST, chckbxNewCheckBox[0], 0, SpringLayout.WEST, textField);
			scrollPane.add(chckbxNewCheckBox[i]);}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}*/
		
		lblNewLabel_3 = new JLabel("Found Projects");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_3, 29, SpringLayout.SOUTH, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_3, 0, SpringLayout.WEST, lblNewLabel);
		lblNewLabel_3.setVisible(false);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		frame.getContentPane().add(lblNewLabel_3);
		panel = new JPanel();
		scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 25, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 19, SpringLayout.EAST, lblNewLabel_3);
		panel.setLayout(new GridLayout(0, 1, 10, 0));
		frame.getContentPane().add(scrollPane);
		path=pathDef;
		System.out.println(pathDef);
		try{
		if(findProjectFolder(pathDef)){
			count =0;
			projectCount=0;
			String[] subDirs = pathDef.split(Pattern.quote(File.separator));
			String fileNameFind=subDirs[subDirs.length-1];
			JViewport port = scrollPane.getViewport();
			radioButton[count] = new JRadioButton();

			radioButton[count].setText(fileNameFind);

			buttonGroup.add(radioButton[count]);
			panel.add(radioButton[count]);
			btnNewButton_2.setEnabled(true);
			++count;
			projectCount++;
			panel.revalidate();
			port.revalidate();
		}
		else
		{
			count =0;
			projectCount=0;
			JViewport port = scrollPane.getViewport();
			File outer=new File(pathDef);
			File[] subdirs=outer.listFiles(directoryFilter);
			if (subdirs.length > 0) {
			for(int i=0;i<subdirs.length;i++)
			{
				if(findProjectFolder(subdirs[i].getAbsolutePath()))
				{
					String fileNameFind=subdirs[i].getName();

					System.out.println(fileNameFind);

					radioButton[count] = new JRadioButton();
					radioButton[count].setText(fileNameFind);
					buttonGroup.add(radioButton[count]);
					panel.add(radioButton[count]);
					++count;
					
					btnNewButton_2.setEnabled(true);
					projectCount++;
				}
			} 
			System.out.println("count: "+count);
			panel.revalidate();
			port.revalidate();
		}
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); 
        progressBar.setIndeterminate(false);
        
		innerPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, innerPanel, 40, SpringLayout.SOUTH, btnNewButton);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -26, SpringLayout.WEST, innerPanel);
		springLayout.putConstraint(SpringLayout.EAST, innerPanel, 0, SpringLayout.EAST, btnNewButton_3);
		
        innerPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.add(progressBar);
        innerPanel.setSize(400, 30);
        innerPanel.setVisible(false);
        innerPanel.setOpaque(true);
        
        
        lblNewLabel1 = new JLabel("Opening project..Please wait.");
        innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
        //innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);
		
	}
	void addCheck(int i,String fileName)
	{
		radioButton[i].setText(fileName);
		radioButton[i].setVisible(true);
		lblNewLabel_3.setVisible(true);
	}


	
}
