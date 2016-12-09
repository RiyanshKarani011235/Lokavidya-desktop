package poi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.iitb.lokavidya.core.utils.GeneralUtils;

public class PresentationToImages {
	
	public ArrayList<String> files = new ArrayList<String>();
	
	public PresentationToImages(String presentation, String location) {
//		String presentationToImagesPythonPath = new File(new File(new File(new File("lib").getAbsolutePath(), "python").
//				getAbsolutePath(), "PresentationToImages"), "PresentationToImages_mark3.py").getAbsolutePath();
//		String presentationToImagesPath = new File(new File(new File("lib").getAbsolutePath(), "python").
//				getAbsolutePath(), "PresentationToImages").getAbsolutePath();
//
//		File fileNamesPath = new File(presentationToImagesPath, "outputFilesList.txt");
//
//		String presentationPath = presentation;
//		System.out.println(presentationToImagesPythonPath);
//
//		String sofficePath = GeneralUtils.findOooPath();
//		System.out.println("soffice path : " + sofficePath);
//		String[] command = {"python", presentationToImagesPythonPath, presentationPath, location, sofficePath};
//		System.out.println(command);
//
//		ProcessBuilder ps = new ProcessBuilder(command);
//		ps.redirectErrorStream(true);
//
//		try {
//			Process pr = ps.start();
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//			    System.out.println(line);
//			}
//			pr.waitFor();
//			in.close();
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		BufferedReader br = null;
//		String everything = null;
//		try {
//			br = new BufferedReader(new FileReader(fileNamesPath));
//		    StringBuilder sb = new StringBuilder();
//		    String line = br.readLine();
//
//		    while (line != null) {
//		        sb.append(line);
//		        sb.append(System.lineSeparator());
//		        line = br.readLine();
//		    }
//		    everything = sb.toString();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//		    try {
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		String[] filesList = everything.split(",");
//		for(int i=0; i<filesList.length; i++) {
//			files.add(new File(location, filesList[i]).getAbsolutePath());
//		}
//
//		System.out.println(files.toString());

		ArrayList<String> tempPPTPathList = new ArrayList<String>();
		File file = new File(presentation);
		FileInputStream out;
		try {
			out = new FileInputStream(file);
			XMLSlideShow ppt = new XMLSlideShow(out);
			List<XSLFSlide> slides = ppt.getSlides();
			
			String tempPath = location;
			
			for(int i=0; i<slides.size(); i++) {
				File tempPPT = new File(location, FilenameUtils.getBaseName(presentation) + "_" + i + "." + FilenameUtils.getExtension(presentation));
				FileUtils.copyFile(new File(presentation), tempPPT);
				keepSlide(tempPPT.getAbsolutePath(), i);
				tempPPTPathList.add(tempPPT.getAbsolutePath());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(int i=0; i<tempPPTPathList.size(); i++) {
			String tempPPTPath = tempPPTPathList.get(i);
			String tempImagePath = new File(location, "img_" + (i+1) + ".jpg").getAbsolutePath();
			GeneralUtils.convertPresentationToImage(tempPPTPath, tempImagePath);
			files.add(tempImagePath);
		}
		
		System.out.println(files.toString());

	}
	
	public static void keepSlide(String presentationPath, int index) {
		File file = new File(presentationPath);
		try {
			if(presentationPath.endsWith(".pptx")){
			XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
			System.out.println(ppt.getSlides().size());
			List<XSLFSlide> slides = ppt.getSlides();
			XSLFSlide selectesdslide= slides.get(index);
			ppt.setSlideOrder(selectesdslide, 0);
			FileOutputStream out = new FileOutputStream(file);
			ppt.write(out);
		    out.close();
		    ppt = new XMLSlideShow(new FileInputStream(file));
		    slides = ppt.getSlides();
		    int i = slides.size()-1;
			while(i>0)
			{
				ppt.removeSlide(i);
				i--;
			}
			out = new FileOutputStream(file);
			//Saving the changes to the presentation
			System.out.println(ppt.getSlides().size());
		    ppt.write(out);
		    out.close();
			}
			else if(presentationPath.endsWith(".ppt")){
				HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(file));
				//System.out.println(ppt.getSlides().size());
				List<HSLFSlide> slides = ppt.getSlides();
				ppt.reorderSlide(index, 1);
			
				//ppt.setSlideOrder(selectesdslide, 0);
				FileOutputStream out = new FileOutputStream(file);
				System.out.println("Yes");
				
			 	ppt.write(out);
				
			    out.close();
			    ppt = new HSLFSlideShow(new FileInputStream(file));
			    slides = ppt.getSlides();
			    System.out.println(slides.size());
			    int i = slides.size()-1;
				while(i>0)
				{
					ppt.removeSlide(i);
					i--;
				}
				out = new FileOutputStream(file);
				//Saving the changes to the presentation
				System.out.println(ppt.getSlides().size());
			    ppt.write(out);
			    out.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {}	
}
