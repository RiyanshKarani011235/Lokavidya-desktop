package instructions;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import com.iitb.lokavidya.core.utils.UserPreferences;

public class LibreOfficeInstructions {
	JFrame frame;
	String pathDef;
	JPanel contentPane;
	
	UserPreferences u;
	
	public LibreOfficeInstructions() {
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

		String installationFilePath = new File("resources", "libreOffice_install_" + osPathString + ".txt").getAbsolutePath();
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
		jtp.setSize(new Dimension(500, 10));
	    jtp.setPreferredSize(new Dimension(500, jtp.getPreferredSize().height));
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
	    
	    Object[] parameters = {jtp};
		JOptionPane.showMessageDialog(null, parameters);
	}
	
	public static void main(String[] args) {
		new GhostScriptInstructions();
	}
	
}
