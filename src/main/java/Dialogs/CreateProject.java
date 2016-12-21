package Dialogs;

import gui.Call;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;

import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.utils.GeneralUtils;

public class CreateProject {
	public String pathDef = null;
	public static String path;
	public static JFrame frame;
	private JFileChooser chooser;
	private JTextField textField;
	private JButton btnNewButton, btnNewButton_2, btnNewButton_3;
	private JLabel lblNewLabel_2;
	private static JTextField textField_1;
	private JButton btnNewButton_1;
	private JLabel lblEnterProjectLocation;
	private static JTextField textField_2;
	public static int progress = 0;
	public ProgressDialog dialog;
	public JProgressBar progressBar;
	public JPanel innerPanel;
	private JLabel lblNewLabel1;

	// private Workspace workspace;
	// public static Workspace workspacestatic;
	/**
	 * Launch the application.
	 */
	class ProgressDialog extends JPanel implements ActionListener, PropertyChangeListener {

		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		public Task task;

		class Task extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				int progress = 0;
				setProgress(0);
				System.out.println("calling creation");
				// CreateProject.projectCreationMethod();
				try {
					String openOfficePath = GeneralUtils.findOooPath();
					if (openOfficePath != null) {
						setProgress(10);
						path = textField_2.getText();
						String filePath = new File(path, textField_1.getText()).getAbsolutePath();
						File newProj = new File(path, textField_1.getText());
						System.out.println("File path: " + filePath);
						if (!Call.workspace.cancelled) {
							if (!newProj.exists()) {
								newProj.mkdir();
							}
							String first = "first";
							setProgress(20);
							// Project p = new Project(textField_1.getText(),
							// path);
							// ProjectCommunicationInstance.launchInstance(p);
							File file = new File(filePath, ".nomedia");
							if (file.createNewFile()) {
								System.out.println("File is created!");
							} else {
								System.out.println("File already exists.");
							}
							setProgress(30);
							Call.workspace.name = textField_1.getText();
							Call.workspace.location = path;
							Call.workspace.path = new File(Call.workspace.location, Call.workspace.name)
									.getAbsolutePath();
							Call.workspace.currentProject = ProjectService.createNewProject(Call.workspace.path, false);
							if (Call.workspace.cancelled) {
								lblNewLabel1.setText("Cancelling creation");
								setProgress(50);
								FileUtils.cleanDirectory(newProj);
								;
								Thread.sleep(1000);
							} else {
								setProgress(65);
								Call.workspace.enable();
								Call.workspace.initializeStates();
								setProgress(75);

								Call.workspace.createProject();
								setProgress(100);
								Thread.sleep(1000);
							}
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

							frame.dispose();
							// innerFrame.dispose();
						} else {
							lblNewLabel1.setText("Cancelling creation");
							setProgress(50);
							Thread.sleep(1000);

							frame.dispose();
						}
					} else {
						frame.dispose();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				return null;
			}

		}

		public void propertyChange(PropertyChangeEvent evt) {

			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				progressBar.setIndeterminate(false);
				progressBar.setValue(progress);
				System.out.println("BINGO");
			}

		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

		ProgressDialog() {

			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			innerPanel.setVisible(true);
			System.out.println("Progress dialog created");
			task = new Task();
			task.addPropertyChangeListener(this);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			task.execute();
		}
	}

	public void directoryChooser() {
		path = new DirectoryChooser().selectedfile;
		textField.setText(path);
	}

	public static void main(String[] args) {

		Call.workspace.cancelled = false;
		CreateProject window = new CreateProject();
		window.frame.setVisible(true);
		System.out.println("Exiting main");

	}

	/**
	 * Create the application.
	 */
	public CreateProject() {
		initialize();
	}

	public static void projectCreationMethod() {

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 442, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		JLabel lblNewLabel = new JLabel("Create New Project");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 26, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 27, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblNewLabel);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);

		innerPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, innerPanel, -51, SpringLayout.EAST, frame.getContentPane());
		innerPanel.setLayout(new BorderLayout(0, 0));
		innerPanel.add(progressBar);
		innerPanel.setSize(400, 30);
		innerPanel.setVisible(false);
		innerPanel.setOpaque(true);

		lblNewLabel1 = new JLabel("Creating project. Please wait....");
		innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
		// innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);
		lblNewLabel_2 = new JLabel("Enter Project Name");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_2, 50, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_2, 0, SpringLayout.WEST, lblNewLabel);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		frame.getContentPane().add(lblNewLabel_2);

		textField_1 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, 0, SpringLayout.NORTH, lblNewLabel_2);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 46, SpringLayout.EAST, lblNewLabel_2);
		springLayout.putConstraint(SpringLayout.EAST, textField_1, 196, SpringLayout.EAST, lblNewLabel_2);

		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		btnNewButton_1 = new JButton("Create");
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -28, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, innerPanel, 0, SpringLayout.NORTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 10, SpringLayout.WEST, frame.getContentPane());
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField_1.getText().equals("")) {
					System.out.println("Text null");
					JOptionPane.showMessageDialog(null, "Enter the project name", "", JOptionPane.INFORMATION_MESSAGE);
				} else if (textField_2.getText().equals("")) {
					System.out.println("Path null");
					JOptionPane.showMessageDialog(null, "Enter the project location", "",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					File f = new File(textField_2.getText(), textField_1.getText());
					if (f.exists()) {
						String str = textField_1.getText() + " already exisits at this location.";
						JOptionPane.showMessageDialog(null, str, "", JOptionPane.INFORMATION_MESSAGE);
					} else {
						dialog = new ProgressDialog();
					}
					System.out.println("Ended CreateProject");

				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		frame.getContentPane().add(btnNewButton_1);

		lblEnterProjectLocation = new JLabel("Enter Project Location");
		springLayout.putConstraint(SpringLayout.NORTH, lblEnterProjectLocation, 25, SpringLayout.SOUTH, textField_1);
		springLayout.putConstraint(SpringLayout.WEST, lblEnterProjectLocation, 0, SpringLayout.WEST, lblNewLabel);
		lblEnterProjectLocation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(lblEnterProjectLocation);

		textField_2 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_2, 0, SpringLayout.NORTH, lblEnterProjectLocation);
		springLayout.putConstraint(SpringLayout.WEST, textField_2, 0, SpringLayout.WEST, textField_1);
		springLayout.putConstraint(SpringLayout.EAST, textField_2, 0, SpringLayout.EAST, textField_1);
		textField_2.setColumns(10);
		pathDef = GeneralUtils.getDocumentsPath();
		textField_2.setText(pathDef);
		frame.getContentPane().add(textField_2);

		btnNewButton_2 = new JButton("..");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path = new DirectoryChooser(pathDef, "none").selectedfile;
				textField_2.setText(path);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_2, -1, SpringLayout.NORTH, lblEnterProjectLocation);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_2, 6, SpringLayout.EAST, textField_2);
		frame.getContentPane().add(btnNewButton_2);

		// Cancel Button
		btnNewButton_3 = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_3, 6, SpringLayout.EAST, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_3, -28, SpringLayout.SOUTH, frame.getContentPane());
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Call.workspace.cancelled = true;
			}
		});
		// disable cancel button - ironstein - 22-11-16
		// frame.getContentPane().add(btnNewButton_3);
	}
}
