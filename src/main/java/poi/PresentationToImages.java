package poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import gui.Call;

public class PresentationToImages {

	public ArrayList<String> files = new ArrayList<String>();

	public PresentationToImages() {

	}
	
	public boolean run(String presentation, String location) {
		System.out.println("presentation : " + presentation);
		System.out.println("location : " + location);
		ArrayList<String> tempPPTPathList = new ArrayList<String>();
		File file = new File(presentation);
		FileInputStream out;
		try {

			if (presentation.endsWith(".pptx")) {
				// for pptx files
				out = new FileInputStream(file);
				System.out.println(out);
				XMLSlideShow ppt = new XMLSlideShow(out);
				List<XSLFSlide> slides = ppt.getSlides();

				String tempPath = location;

				for (int i = 0; i < slides.size(); i++) {
					File tempPPT = new File(location, FilenameUtils.getBaseName(presentation) + "_" + i + "."
							+ FilenameUtils.getExtension(presentation));
					System.out.println("tempPPT : " + tempPPT);
					FileUtils.copyFile(new File(presentation), tempPPT);
					keepSlide(tempPPT.getAbsolutePath(), i);
					tempPPTPathList.add(tempPPT.getAbsolutePath());

					if (Call.workspace.cancelled) {
						// cancelled
						return false;
					}
				}

			} else if (presentation.endsWith(".ppt")) {
				// for ppt files
				out = new FileInputStream(file);
				System.out.println(out);
				HSLFSlideShow ppt = new HSLFSlideShow(out);
				List<HSLFSlide> slides = ppt.getSlides();

				String tempPath = location;

				for (int i = 0; i < slides.size(); i++) {
					File tempPPT = new File(location, FilenameUtils.getBaseName(presentation) + "_" + i + "."
							+ FilenameUtils.getExtension(presentation));
					System.out.println("tempPPT : " + tempPPT);
					FileUtils.copyFile(new File(presentation), tempPPT);
					keepSlide(tempPPT.getAbsolutePath(), i);
					tempPPTPathList.add(tempPPT.getAbsolutePath());
					if (Call.workspace.cancelled) {
						// cancelled
						return false;
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < tempPPTPathList.size(); i++) {
			String tempPPTPath = tempPPTPathList.get(i);
			String tempImagePath = new File(location, "img_" + (i + 1) + ".jpg").getAbsolutePath();
			GeneralUtils.convertPresentationToImage(tempPPTPath, tempImagePath);
			files.add(tempImagePath);
		}

		System.out.println(files.toString());
		return true;
	}

	public static void keepSlide(String presentationPath, int index) {
		File file = new File(presentationPath);
		try {
			if (presentationPath.endsWith(".pptx")) {
				XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
				System.out.println(ppt.getSlides().size());
				List<XSLFSlide> slides = ppt.getSlides();
				XSLFSlide selectesdslide = slides.get(index);
				ppt.setSlideOrder(selectesdslide, 0);
				FileOutputStream out = new FileOutputStream(file);
				ppt.write(out);
				out.close();
				ppt = new XMLSlideShow(new FileInputStream(file));
				slides = ppt.getSlides();
				int i = slides.size() - 1;
				while (i > 0) {
					ppt.removeSlide(i);
					i--;
				}
				out = new FileOutputStream(file);
				// Saving the changes to the presentation
				System.out.println(ppt.getSlides().size());
				ppt.write(out);
				out.close();
			} else if (presentationPath.endsWith(".ppt")) {
				HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(file));
				// System.out.println(ppt.getSlides().size());
				List<HSLFSlide> slides = ppt.getSlides();
				ppt.reorderSlide(index + 1, 1);

				// ppt.setSlideOrder(selectesdslide, 0);
				FileOutputStream out = new FileOutputStream(file);
				System.out.println("Yes");

				ppt.write(out);

				out.close();
				ppt = new HSLFSlideShow(new FileInputStream(file));
				slides = ppt.getSlides();
				System.out.println(slides.size());
				int i = slides.size() - 1;
				while (i > 0) {
					ppt.removeSlide(i);
					i--;
				}
				out = new FileOutputStream(file);
				// Saving the changes to the presentation
				System.out.println(ppt.getSlides().size());
				ppt.write(out);
				out.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// String fileName = "/Users/ironstein/Documents/demoPresentation.ppt";
		String fileName = "/Users/ironstein/Documents/tamil.pptx";
		try {
			// XMLSlideShow p = new XMLSlideShow(new FileInputStream(fileName));
			HSLFSlideShow p = new HSLFSlideShow(new FileInputStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
