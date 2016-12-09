package Xuggler;

public class VidCapture {
	public VideoCapture videoCapture=new VideoCapture();
	public VidCapture(String outfile)
	{
		videoCapture.addFile(outfile);
	}
	public void start()
	{
		videoCapture.start();
	}
	public void stop()
	{
		videoCapture.stop();
	}
}
