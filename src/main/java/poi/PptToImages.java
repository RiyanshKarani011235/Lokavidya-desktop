/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package poi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.SlideShow;

import gui.Call;

/**
*
* @author pc
*/
	public class PptToImages {

/**
* @param args the command line arguments
*/
	public ArrayList<String>files;	
	boolean useNewImplementation = true;

	public  PptToImages(String presentation,String location) throws Exception{
		files=new ArrayList<String>();
		
		// TODO code application logic here
		if(useNewImplementation) {
			// new Implementation
			PresentationToImages p = new PresentationToImages(presentation, location);
			files = p.files;
		} else {
			// old implementation
			FileInputStream is = new FileInputStream(presentation);
			HSLFSlideShow ppt = new HSLFSlideShow(is);
			is.close();
			
			Dimension pgsize = ppt.getPageSize();
			
			List<HSLFSlide> slide = ppt.getSlides();
			for (int i = 0; i < slide.size(); i++) {
			
				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, 1);
				
				Graphics2D graphics = img.createGraphics();
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
				
				graphics.setColor(Color.white);
				graphics.clearRect(0, 0, pgsize.width, pgsize.height);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				
				// render
				slide.get(i).draw(graphics);
				
				// save the output
				String fileName=new File(location,("img_"+(Integer.toString(i+1))+".jpg")).getAbsolutePath();
				//fileName=new File(location,fileName).getAbsolutePath();
				files.add(fileName);
				FileOutputStream out = new FileOutputStream(fileName);
				javax.imageio.ImageIO.write(img, "jpg", out);
				out.close();
			}
		}
	}
	
	public static void main(String[] args) {
        
        try {
         //  new PptToImages("C:\\Users\\deysaikat95\\Desktop\\ABCDEF.ppt","C:\\Users\\deysaikat95\\Documents");
        } catch (Exception ex) {
            Logger.getLogger(PptxToImages.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}