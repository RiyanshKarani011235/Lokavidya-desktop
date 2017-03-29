package poi;

import java.util.ArrayList;

public class PptToImages {
	
	public ArrayList<String>files;
	
	private String mPresentation;
	private String mLocation;
	
	public void getImages() {}

	public PptToImages(String presentation,String location) throws Exception {		
		mPresentation = presentation;
		mLocation = location;
	}
	
	public boolean run() {
		PresentationToImages p = new PresentationToImages();
		boolean b = p.run(mPresentation, mLocation);
		files = p.files;
		return b;
	}
	
}