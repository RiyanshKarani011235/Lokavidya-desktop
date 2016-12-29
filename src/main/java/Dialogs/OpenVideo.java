package Dialogs;

import gui.Call;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.apache.commons.io.FilenameUtils;

import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Segment.SegmentType;
import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.utils.GeneralUtils;
import com.iitb.lokavidya.core.utils.UserPreferences;

import Dialogs.OpenProject.ProgressDialog;
import Dialogs.OpenProject.ProgressDialog.Task;

public class OpenVideo {
	public JFrame frame;
	public String pathDef;
	public String path;
	public ProgressDialog dialog;
	private JButton btnNewButton_1;
	private JTextField textField_2;

	private JPanel innerPanel;
	private JProgressBar progressBar;
	private JLabel lblNewLabel1;

	class ProgressDialog extends JPanel implements ActionListener, PropertyChangeListener {

		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		public Task task;
		public boolean isTaskCancellable = true;

		class Task extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				Call.workspace.startOperation();
				path = textField_2.getText();
				String vidName = new File(path).getName();
				setProgress(10);
				Segment video = new Segment(path, SegmentType.VIDEO, Call.workspace.currentProject.getProjectURL());
				
				if(!video.getVideo().videoValid) {
					// video import was interrupted
					return null;
				}
				
				// cannot cancel the import the video has been converted
				isTaskCancellable = false;
				
				setProgress(50);
				Call.workspace.currentProject.addSegment(video);
				ProjectService.persist(Call.workspace.currentProject);
				Call.workspace.repopulateProject();
				setProgress(100);
				Thread.sleep(1000);
				System.out.println(path);
				Call.workspace.endOperation();
				frame.dispose();
				return null;
			}

		}

		ProgressDialog() {

			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {

			    @Override
			    public void windowClosing(WindowEvent e) {
			    	if(isTaskCancellable) {
			    		int confirm = JOptionPane.showOptionDialog(
			    			null, "Are You Sure to cancel the import?", 
				            "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
				            JOptionPane.QUESTION_MESSAGE, null, null, null);
					        if (confirm == 0) {
					        	if (task != null) {
					        		System.out.println("cancelling task");
					        		task.cancel(true);
					        		Call.workspace.endOperation();
									frame.dispose();
					        	}
					        }
			    	} else {
			    		String message = 
			    				"Sorry, the import cannot be cancelled at this time because\n" +
			    				"the project has already been modified and cancelling import at\n" + 
			    				"this time might corrupt the project. If you do not want to\n" + 
			    				"include the following slides in this project, you can delete\n" + 
			    				"the imported slides after import is complete.\n\n" + 
			    				"The import will complete soon.";
		        		JOptionPane.showMessageDialog(null, message);
		        	}
			    }
			});
			
			UserPreferences u = new UserPreferences();
			if (u.getDisplayInstruction("openVideo").equals("n")) {
				frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				JCheckBox checkbox = new JCheckBox("Don't show again");
				String message = "The video will be appended to the end of the project.";
				Object[] parameters = { message, checkbox };
				JOptionPane.showMessageDialog(this, parameters);

				// check if checkbox clicked
				if (checkbox.isSelected()) {
					// selected, disable this message in the future
					u.updateDisplayInstruction("openVideo", "y");
				} else {
					// not selected
				}
			}

			innerPanel.setVisible(true);
			System.out.println("Progress dialog created");
			task = new Task();
			task.addPropertyChangeListener(this);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			task.execute();
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
	}

	public static void copyFile(File from, File to) {
		// Files.delete(to.toPath());

		try {
			Files.copy(from.toPath(), to.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Call.workspace.cancelled = false;
					OpenVideo window = new OpenVideo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public OpenVideo() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 442, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		JLabel lblNewLabel = new JLabel("Open Video");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel);

		textField_2 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_2, 73, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField_2, 161, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 1, SpringLayout.NORTH, textField_2);
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, -6, SpringLayout.WEST, textField_2);

		textField_2.setColumns(10);

		pathDef = GeneralUtils.getDocumentsPath();
		textField_2.setText(pathDef);
		frame.getContentPane().add(textField_2);

		JButton btnNewButton_2 = new JButton(" ... ");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_2, 74, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField_2, -3, SpringLayout.WEST, btnNewButton_2);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_2, -10, SpringLayout.EAST, frame.getContentPane());
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path = new DirectoryChooser(pathDef, "mp4").selectedfile;

				textField_2.setText(path);
			}
		});
		frame.getContentPane().add(btnNewButton_2);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);
		innerPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, innerPanel, -28, SpringLayout.EAST, frame.getContentPane());

		innerPanel.setLayout(new BorderLayout(0, 0));
		innerPanel.add(progressBar);
		innerPanel.setSize(400, 30);
		innerPanel.setVisible(false);
		innerPanel.setOpaque(true);

		lblNewLabel1 = new JLabel("Opening video..Please wait.");
		innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
		// innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);

		btnNewButton_1 = new JButton("Import");
		springLayout.putConstraint(SpringLayout.SOUTH, innerPanel, 0, SpringLayout.SOUTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 27, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -28, SpringLayout.SOUTH, frame.getContentPane());
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (new File(textField_2.getText()).isDirectory() || textField_2.getText() == ""
						|| !(textField_2.getText().endsWith(".mp4") || textField_2.getText().endsWith(".MP4"))) {
					JOptionPane.showMessageDialog(null, "Enter the video location", "",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					dialog = new ProgressDialog();
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(btnNewButton_1);
	}

}
