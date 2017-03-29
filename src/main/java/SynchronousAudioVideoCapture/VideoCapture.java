package SynchronousAudioVideoCapture;

import java.io.File;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import Xuggler.CaptureScreenToFile;
import gui.Call;

public class VideoCapture implements Runnable {
	private String mVideoPath;
	private CaptureScreenToFile mVideoEncoder;
	private int mTimeBetweenFrames;
	private SynchronousAudioVideoCapture mCapture;
	private long mDuration;

	public VideoCapture(String videoPath, int videoFrameRate, SynchronousAudioVideoCapture capture) {
		mVideoPath = videoPath;
		mCapture = capture;
		mTimeBetweenFrames = 1000/videoFrameRate;
		File tempFolder = new File(System.getProperty("java.io.tmpdir"), "ScreenRec");
		tempFolder.mkdir();
		mVideoEncoder = new CaptureScreenToFile(mVideoPath);
	}
	
	public int getFrameRate() {
		return (int) 1000/mTimeBetweenFrames;
	}
	
	public void setFrameRate(int videoFrameRate) {
		mTimeBetweenFrames = 1000/videoFrameRate;
	}

	public void stop() {
		mVideoEncoder.setPauseTime(mCapture.getCurrentTime() - mDuration);
		mVideoEncoder.closeStreams();
	}

	public void run() {
		
		System.out.println("video capture run called");
		
		// update video duration
		mDuration += mTimeBetweenFrames;
		
		// capture frame
		mVideoEncoder.encodeImage(mVideoEncoder.takeSingleSnapshot());
		
		// if paused, then cancel schedule for the next call
		if(mCapture.getState() == SynchronousAudioVideoCapture.States.PAUSED) {
			System.out.println("videoCapture pausing");
			mCapture.getVideoCaptureScheduler().cancel(true);
		}
	}
	
}
