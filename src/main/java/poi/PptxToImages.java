package poi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.poi.xslf.usermodel.*;

import gui.Call;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.sl.usermodel.SlideShow;



public class PptxToImages {
	public String location,filename;
	public ArrayList<String>files;
	
	boolean useNewImplementation = true;
	
	public void getImages() {}

	public PptxToImages(String presentation,String location) throws Exception {
		
		if(useNewImplementation) {
			// new Implementation 
			PresentationToImages p = new PresentationToImages(presentation, location);
			files = p.files;
		} else {
			// old implementation
			//String[] subDirs = new String[10];
	        presentation=new File(presentation).getAbsolutePath();
	        //filename=new File(presentation).getName();
	            
			System.out.println(presentation);

			File file=new File(presentation);
		    XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
		      
		    //getting the dimensions and size of the slide 
		    Dimension pgsize = ppt.getPageSize();
		    List<XSLFSlide> slide = ppt.getSlides();
		    files=new ArrayList<String>();
		    for (int i = 0; i < slide.size(); i++) {
		       BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.TYPE_INT_RGB);
		       Graphics2D graphics = img.createGraphics();
		
		       //clear the drawing area
		                  
		       graphics.setPaint(Color.white);
		       graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
		
		       //render
		       slide.get(i).draw(graphics);
		         
		       //creating an image file as output
		       //String projectPath=new File(path).getParentFile().getAbsolutePath();
		       String fileName=new File(location,("img_"+(Integer.toString(i+1))+".jpg")).getAbsolutePath();
		       FileOutputStream out = new FileOutputStream(fileName);
		       System.out.println(fileName);
		       javax.imageio.ImageIO.write(img, "jpg", out);
		       ppt.write(out);
		       out.close();
		   }
		}       
	}
	
    public static void main(String[] args) {
    
        try {
            new PptxToImages("/home/frg/Documents/Biogas.pptx","/home/frg/Documents");
        } catch (Exception ex) {
            Logger.getLogger(PptxToImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}