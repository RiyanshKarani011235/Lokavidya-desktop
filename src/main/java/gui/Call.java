package gui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.iitb.lokavidya.core.utils.GeneralUtils;
import com.iitb.lokavidya.core.utils.UIUtils;

import Hotkey.Hotkey;
import instructions.InstallationInstructions;

public class Call {
	
	public static Workspace workspace; 
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String osname = System.getProperty("os.version");
		System.out.println(osname);
		
		GeneralUtils.startUp(); 
		UIUtils.setLookAndFeel();
	
		workspace=new Workspace();
		workspace.setVisible(true);
		//workspace.setAlwaysOnTop(true);
		Hotkey h=new Hotkey();
		
		new InstallationInstructions();
	}

}
