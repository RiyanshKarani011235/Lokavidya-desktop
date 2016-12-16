package poi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

public class PdfToImages {
	
	public static void convert(String pdfFilename, String outputLocation) {

		PDDocument document;
		try {
			document = PDDocument.loadNonSeq(new File(pdfFilename), null);
			List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
			int page = 0;
			for (PDPage pdPage : pdPages)
			{ 
			    ++page;
			    BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
			    ImageIOUtil.writeImage(bim, pdfFilename + "-" + page + ".jpg", 300);
			}
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		convert("/Volumes/jarvis/Desktop/VC bound proof.pdf", "/users/ironstein/desktop");
	}
	
}
