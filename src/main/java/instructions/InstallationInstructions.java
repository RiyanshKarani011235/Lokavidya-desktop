package instructions;

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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.View;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import Dialogs.OpenProject.ProgressDialog;
import com.iitb.lokavidya.core.utils.UserPreferences;
import gui.Call;

import static org.apache.poi.hslf.record.OEPlaceholderAtom.Object;

public class InstallationInstructions {
	
	JFrame frame;
	String pathDef;
	JPanel contentPane;
	
	UserPreferences u;
	
	public InstallationInstructions() {
		u = new UserPreferences();
		if(u.getDisplayInstruction("readInstructions").equals("n")) {
			init();
		}
	}
	
	public InstallationInstructions(boolean checkUserPreferences) {
		u = new UserPreferences();
		if(!checkUserPreferences) {
			init();
		} else if(u.getDisplayInstruction("readInstructions").equals("n")) {
			init();
		}
		
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
		
		JTextPane jtp = new JTextPane();
		Document doc = jtp.getDocument();
		for(int i=0; i<instructionsList.length; i++) {
			try {
				doc.insertString(doc.getLength(), instructionsList[i] + "\n", new SimpleAttributeSet());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// set fixed size
		jtp.setSize(new Dimension(800, 10));
	    jtp.setPreferredSize(new Dimension(800, jtp.getPreferredSize().height));
	    jtp.setBorder(
    		javax.swing.BorderFactory.createCompoundBorder(
    			javax.swing.BorderFactory.createTitledBorder(
	    	         null, null,
	    	         javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
	    	         javax.swing.border.TitledBorder.DEFAULT_POSITION,
	    	         new java.awt.Font("Verdana", 1, 11)
	    	      ),
	    	      javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3)
	    	)
    	);
	    JCheckBox checkbox = new JCheckBox("Don't show again");
		Object[] parameters = {jtp, checkbox};

		JOptionPane.showMessageDialog(null, parameters);

		// check if checkbox clicked
		if(checkbox.isSelected()) {
			// selected, disable this message in the future
			u.updateDisplayInstruction("readInstructions", "y");
		} else {
			// not selected
		}
	}
	
	public static void main(String[] args) {
		new InstallationInstructions();
	}
	
}
