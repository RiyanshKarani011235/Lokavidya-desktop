package com.iitb.lokavidya.core.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.*;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import com.iitb.lokavidya.core.data.Audio;
import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Slide;
import com.iitb.lokavidya.core.data.Video;

import Dialogs.JEnhancedOptionPane;
import gui.Call;
import gui.CustomPanel;
import libreoffice.LibreConvert;

public class GeneralUtils {

	/*
	 * Generate Random Number of Digit Length
	 * 
	 * @param int digitLength
	 * 
	 * @return long_number_with_digit length
	 */
	public static Long generateRandomNumber(int digitLength) {

		Random generator = new Random();
		generator.setSeed(System.currentTimeMillis());

		return (long) (100000 + generator.nextInt(900000));

	}

	public static void startUp() {
		String dirpath = new File("").getAbsolutePath();

		File dir = new File(dirpath);
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				if (f.getName().endsWith("flv") || (f.getName().endsWith("log")) || (f.getName().endsWith("txt"))) {
					System.out.println(f.getAbsolutePath());
					f.delete();
				}
			}
		}
	}

	public static String getDocumentsPath() {
		String pathDef = null;
		String Os = System.getProperty("os.name");
		if (Os.startsWith("Windows 10") || Os.startsWith("Windows 8") || Os.startsWith("Windows 7")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "Documents";
		} else if (Os.startsWith("Windows")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "My Documents";
		} else if (Os.startsWith("Linux")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "Documents";
		}

		else if (Os.startsWith("Mac")) {
			pathDef = System.getProperty("user.home") + File.separatorChar + "Documents";
		}
		return pathDef;
	}

	public static void openFile(File fileName) {
		if (Desktop.isDesktopSupported()) {

			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.open(fileName);
			} catch (java.io.IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void stopOfficeInstance() {
		String openOfficePath = GeneralUtils.findOooPath();
		if (System.getProperty("os.name").contains("Windows")) {
			String[] command = { "taskkill", "/F", "/IM", openOfficePath };
			GeneralUtils.runProcess(command);
		}
	}

	public static long getFolderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += getFolderSize(file);
		}
		return length;
	}

	public static int getNumberofSlides(String pptURL) {
		XMLSlideShow ppt = null;
		int no = 0;
		try {
			ppt = new XMLSlideShow(new FileInputStream(new File(pptURL)));
			no = (ppt.getSlides().size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return no;
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

	public static String createNewPresentation(String path, String name) {
		File ppt = new File(new File("resources").getAbsolutePath(), "NewPresentation.odp");
		File f = new File(path, (name + ".odp"));
		copyFile(ppt, f);
		return f.getAbsolutePath();
	}

	public static String createBlankSlide(String path, String name) {
		File ppt = new File(new File("resources").getAbsolutePath(), "BlankSlide.odp");
		File f = new File(path, (name + ".odp"));
		copyFile(ppt, f);
		return f.getAbsolutePath();
	}

	public static boolean convertImageToPresentation(String imgPath, String pptPath) {
		System.out.println("Converting Image To Presentation");
		String params[] = { "java", "-jar", "resources/createppt.jar", pptPath, imgPath };
		runProcess(params);
		System.out.println("Complete");
		return true;
	}

	public static File search(File dir, String file) {
		File y;
		if (dir.exists()) {
			try {
				System.out.println(dir.toString());
				File[] files = dir.listFiles();
				if (files == null) {
					System.out.println("files is null");
				}
				System.out.println("files : " + files.toString());
				for (File x : files) {
					System.out.println(x.getName());
					if (x.isDirectory()) {
						y = search(x, file);
						if (y != null)
							return y;
					} else if (x.getName().equals(file)) {
						return x;
					}
					// System.out.println(x.toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else
			System.out.println("Cannot find folder");
		return null;
	}

	public static String findOooPath() {

		// setting default values
		String openOfficePath = "", execName = "";
		if (System.getProperty("os.name").contains("Linux")) {
			openOfficePath = "/usr/lib/libreoffice/program/simpress";
			execName = "simpress";
		} else if (System.getProperty("os.name").contains("Windows")) {
			openOfficePath = "C:\\Program Files (x86)\\LibreOffice 5\\program\\simpress.exe";
			execName = "simpress.exe";
		} else if (System.getProperty("os.name").startsWith("Mac")) {
			openOfficePath = "/Applications/LibreOffice.app/Contents/MacOS/soffice";
			execName = "soffice";
		}

		// check if the default value is correct
		if ((openOfficePath != "") && (new File(openOfficePath).exists())) {
			System.out.println("OpenOffice path found" + openOfficePath);
			return openOfficePath;
		}
		
		// if default values not correct, check in userPreferences.json
		UserPreferences u = new UserPreferences();
		openOfficePath = u.getPath("OpenOffice");
		if(!(openOfficePath == null)) {
			if((new File(openOfficePath).exists()) && (openOfficePath.endsWith(execName))) {
				return openOfficePath;
			} else {
				// write null to openOffice path if the currently 
				// written path does not exist or incorrect
				u.updatePath("OpenOffice", "");
			}
		}
		
		// if not in userPreferences.json, ask the user
		openOfficePath = getOoPathFromUser(execName);
		System.out.println("before updating : " + openOfficePath);
		u.updatePath("OpenOffice", openOfficePath);
		return openOfficePath;
	}

	public static String getOoPathFromUser(String execName) {
		String openOfficePath;
		String vidName = (String) JEnhancedOptionPane.showInputDialog(
				"Lokavidya could not find your installed location of Libreoffice. Please enter the location\n" + 
				"You need to enter the name of the folder that contains the executable named " + execName + "\n" +
				"An example location would be \"C:\\Program Files (x86)\\LibreOffice 5\\program\\\" for Windows " + 
				"or /usr/lib/libreoffice/program/ for Linux or /Applications/LibreOffice.app/Contents/MacOs for Mac OS",
				new Object[] { "Run", "Discard" });
		if (vidName != null) {
			System.out.println(vidName);
			File f = new File(vidName), found = null;
			if (f.exists()) {
				found = search(f, execName);
				if ((found != null) && (found.exists())) {
					openOfficePath = found.getAbsolutePath();
					System.out.println("Path found: " + openOfficePath);
					return openOfficePath;
				}
			}
		}
		JOptionPane.showMessageDialog(null, "Sorry, Lokavidya cannot find Libreoffice.", "",
			JOptionPane.INFORMATION_MESSAGE);
		return null;
	}
	
	public static String findGhostScriptPath() {
		
		ArrayList<String> execName = new ArrayList<String>();

		// setting to default values
		String ghostScriptPath = "/usr/local/bin/gsc";
		if(System.getProperty("os.name").contains("Windows")) {
			// windows
			ghostScriptPath = "";
			execName.add("gswin32c.exe");
			execName.add("gswin64c.exe");
		} else {
			execName.add("gsc");
			if(!System.getProperty("os.name").toLowerCase().contains("mac")) {
				// linux
				ghostScriptPath = "/usr/bin/gsc";
			} else {
				// mac
			}
		}
		
		// checking if default values are correct
		if ((ghostScriptPath != "") && new File(ghostScriptPath).exists()) {
			System.out.println("OPen Office path found" + ghostScriptPath);
			return ghostScriptPath;
		}
		
		// if default values not correct, check in userPreferences.json
		UserPreferences u = new UserPreferences();
		ghostScriptPath = u.getPath("ghostScript");
		if(!(ghostScriptPath == null)) {
			if((new File(ghostScriptPath).exists())) {
				// checking if ghostScriptPath in userPreferences.json is correct
				for(int i=0; i<execName.size(); i++) {
					if(ghostScriptPath.endsWith(execName.get(i))) {
						return ghostScriptPath;
					}
				}
			} else {
				// write null to ghostScript path if the currently 
				// written path does not exist or is incorrect
				u.updatePath("ghostScript", "");
			}
		}
		
		// if not in userPreferences.json, ask user for the path
		ghostScriptPath = getGhostScriptPathFromUser(execName);
		System.out.println("ghostScriptPath : " + ghostScriptPath);
		if(ghostScriptPath == null) {
			// user did not enter the correct path
			u.updatePath("ghostScript", "");
		} else {
			// user entered the correct path
			u.updatePath("ghostScript", ghostScriptPath);
		}
		
		return ghostScriptPath;
	}
	
	public static String getGhostScriptPathFromUser(ArrayList<String> execName) {
		String ghostScriptPath;
		
		// building a message String
		String messageString = "Lokavidya could not find your installed location of ghostScript. Please enter the location.\n" +
				"You need to enter the name of the folder that contains the executable named \"";
		for(int i=0; i<execName.size(); i++) {
			messageString += execName.get(i) + "\"";
			if(i != (execName.size() - 1)) {
				messageString += " or \"";
			}
		} messageString += ".\n";
		messageString += "An example location would be\n" + 
			"C:\\Program Files\\gs\\ for Windows or\n" + 
			"/usr/bin/ for Linux or\n" + 
			"/usr/local/bin/ for Mac OS";
		
		ghostScriptPath = (String) JEnhancedOptionPane.showInputDialog(
				messageString,
				new Object[] { "Run", "Discard" });
		if (ghostScriptPath != null) {
			System.out.println(ghostScriptPath);
			File f = new File(ghostScriptPath);
			File found = null;
			if (f.exists() && f.isDirectory()) {
				for(int i=0; i<execName.size(); i++) {
					found = search(f, execName.get(i));
					if ((found != null) && (found.exists())) {
						ghostScriptPath = found.getAbsolutePath();
						System.out.println("Path found: " + ghostScriptPath);
						return ghostScriptPath;
					}
				}
				
			} 
		}
		JOptionPane.showMessageDialog(null, "Sorry, Lokavidya cannot find GhostScript executable at the location provided.", "",
			JOptionPane.INFORMATION_MESSAGE);
		return null;
	}

	public static boolean convertPresentationToImage(String pptPath, String imagePath) {
		// TODO Auto-generated method stub
		LibreConvert l = new LibreConvert();
		String openOfficePath = findOooPath();
		if (openOfficePath != null) {
			l.launchOfficeInstanceIncognito(pptPath, openOfficePath);
			l.saveAsURL(imagePath);
			// l.stopOfficeInstance();
		}
		return true;
	}

	public static boolean convertPptToOdp(String pptPath, String odpPath) {
		// TODO Auto-generated method stub

		LibreConvert l = new LibreConvert();
		String openOfficePath = findOooPath();
		if (openOfficePath != null) {
			l.launchOfficeInstanceIncognito(pptPath, openOfficePath);
			l.saveAsURL(odpPath);
			// l.stopOfficeInstance();
		}
		return true;
	}

	public static void cleanUp(Project p) {
		try {
			List<String> necessaryList = new ArrayList<String>();
			if (p != null) {
				for (Segment s : p.getOrderedSegmentList()) {
					Slide sl;
					Audio au;
					Video v;
					if ((sl = s.getSlide()) != null) {
						necessaryList.add(sl.getImageURL());
						necessaryList.add(sl.getPptURL());
						if ((au = sl.getAudio()) != null) {
							necessaryList.add(au.getAudioURL());
						}
					}
					if ((v = s.getVideo()) != null)
						necessaryList.add(v.getVideoURL());
				}
				File opf = new File(p.getProjectURL(), (p.getProjectName() + ".mp4"));
				necessaryList.add(opf.getAbsolutePath());
				File dir = new File(p.getProjectURL());
				List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE,
						TrueFileFilter.INSTANCE);
				for (File f : files) {
					if (!(necessaryList.contains(f.getAbsolutePath())) && (!f.getAbsolutePath().endsWith("json"))
							&& (!f.getAbsolutePath().endsWith("hash"))) {
						System.out.println(f.getAbsolutePath());
						Files.deleteIfExists(f.toPath());
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createZip(String fileLocation, String zipLocation) {

		System.out.println(fileLocation);
		try {
			zipLocation = new File(zipLocation, (new File(fileLocation).getName() + ".zip")).getAbsolutePath();
			if (new File(zipLocation).exists())
				new File(zipLocation).delete();
			System.out.println(zipLocation);
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipLocation);

			// Folder to add

			// Initiate Zip Parameters which define various properties such
			// as compression method, etc.
			ZipParameters parameters = new ZipParameters();

			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			// Set the compression level
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			// Add folder to the zip file
			zipFile.addFolder(fileLocation, parameters);

		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static void extractZip(String zipLocation, String destination) {
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipLocation);

			// Extracts all files to the path specified
			zipFile.extractAll(destination);

		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static boolean containsWhiteSpace(final String testCode) {
		if (testCode != null) {
			for (int i = 0; i < testCode.length(); i++) {
				if (Character.isWhitespace(testCode.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean runProcess(String[] command) {
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			// print command
			System.out.println("running process");
			System.out.print(" ************* runProcess : running command : ");
			for (String s : command) {
				System.out.print(s + " ");
			}
			System.out.println("\n***************************************************");

			pr = run.exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT");
			errorGobbler.start();
			outputGobbler.start();
			System.out.println("waiting for exit value");
			int exitVal = pr.waitFor();
			System.out.println("Exitval is " + exitVal);
			System.out.println("Finished process");
			pr.destroy();
			return true;
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public static String convertToMinSecFormat(long duration) {
		long mins = duration / 60;
		long seconds = duration % 60;
		String timeOutput = "";
		if (mins < 10) {
			// System.out.print("0"+mins+":");
			timeOutput = timeOutput + "0" + mins + ":";
		} else {
			// System.out.print(""+mins+":");
			timeOutput = timeOutput + mins + ":";
		}
		if (seconds < 10) {
			// System.out.print("0"+seconds);
			timeOutput = timeOutput + "0" + seconds;
		} else {
			// System.out.print(""+seconds);
			timeOutput = timeOutput + seconds;
		}
		return timeOutput;
	}

	public static long runProbe(String[] command) {
		Runtime run = Runtime.getRuntime();
		long duration = 0;
		Process pr;
		try {
			for (String s : command) {
				System.out.print(s + " ");
			}
			System.out.println();
			pr = run.exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

			// read the output from the command
			// System.out.println("Here is the standard output of the
			// command:\n");
			String s = null;

			/*
			 * while ((s = stdInput.readLine()) != null) {
			 * System.out.println("Line1: "+s); if( Double.parseDouble(s)){
			 * double tempDuration = Double.parseDouble(s); duration =
			 * (long)tempDuration; } } while ((s = stdError.readLine()) != null)
			 * { System.out.println(s); }
			 */
			if ((s = stdInput.readLine()) != null) {
				double tempDuration = Double.parseDouble(s);
				duration = (long) tempDuration;
			}
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return duration;
	}

	public static void main(String args[]) {

	}
}
