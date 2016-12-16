package Dialogs;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DirectoryChooser extends JPanel {

	/**
	 * Create the panel.
	 */
	private JFileChooser chooser;
	public String selectedfile;
	public DirectoryChooser() {
		String Os = System.getProperty("os.name");
		if (Os.startsWith("Mac")) 
			System.setProperty("apple.awt.fileDialogForDirectories", "true");
		chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Choose folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	      //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
	      //System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
	    	//selectedfile=chooser.getName(chooser.getCurrentDirectory());
	    	File f=chooser.getSelectedFile();
	    	selectedfile=f.getAbsolutePath();
	    	//selectedfile+=chooser.getName(chooser.getSelectedFile());
	    	//System.out.println(selectedfile);
	    } else {
	      System.out.println("No Selection ");
	    }
	    add(chooser);
	    //frame.getContentPane().add(chooser);
	}
	public DirectoryChooser(String path, String type){
		chooser = new JFileChooser();
		File fTemp = new File(path);
		if (type.equals("ppt")) {
			FileNameExtensionFilter pptFilter = new FileNameExtensionFilter("ppt files (*.ppt, *.pptx)", "ppt","pptx");
			chooser.setFileFilter(pptFilter);
			chooser.setAcceptAllFileFilterUsed(false);
		    chooser.setDialogTitle("Choose presentation");
		} else if (type.equals("mp4")) {
			FileNameExtensionFilter pptFilter = new FileNameExtensionFilter("mp4 files (*.mp4)", "mp4");
			chooser.setFileFilter(pptFilter);
			chooser.setAcceptAllFileFilterUsed(false);
		    chooser.setDialogTitle("Choose video");
		} else if (type.equals("zip")){
			FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("zip files (*.zip)", "zip");
			//chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(zipFilter);
			chooser.setAcceptAllFileFilterUsed(true);
		    chooser.setDialogTitle("Choose video");
		} else if(type.equals("pdf")) {
			FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("pdf files (*.pdf)", "pdf");
			chooser.setFileFilter(pdfFilter);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setDialogTitle("choose PDF");
		} else {
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setDialogTitle("Choose folder");
		}
	    chooser.setCurrentDirectory(fTemp);
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	      //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
	      //System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
	    	//selectedfile=chooser.getName(chooser.getCurrentDirectory());
	    	File f=chooser.getSelectedFile();
	    	selectedfile=f.getAbsolutePath();
	    	//selectedfile+=chooser.getName(chooser.getSelectedFile());
	    	//System.out.println(selectedfile);
	    } else {
	      System.out.println("No Selection ");
	    }
	    add(chooser);
	}
	
	public static void main(String args[])
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DirectoryChooser window = new DirectoryChooser();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
