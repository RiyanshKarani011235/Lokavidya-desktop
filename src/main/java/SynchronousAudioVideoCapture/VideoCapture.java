package SynchronousAudioVideoCapture;

public abstract class VideoCapture implements Runnable {
	
	abstract public int getFrameRate();
	abstract public void setFrameRate(int frameRate);
	abstract public void stop();
	abstract public void run();
	abstract public boolean isBusy();
}
