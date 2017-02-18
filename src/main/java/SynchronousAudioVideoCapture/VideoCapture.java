package SynchronousAudioVideoCapture;

import java.io.File;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import Xuggler.CaptureScreenToFile;
import gui.Call;

public class VideoCapture implements Runnable {
	private String mVideoPath;
	private CaptureScreenToFile mVideoEncoder;
	private int mTimeBetweenFrames;
	private SynchronousAudioVideoCapture_mark2 mCapture;
	private long mDuration;

	public VideoCapture(String videoPath, int videoFrameRate, SynchronousAudioVideoCapture_mark2 capture) {
		mVideoPath = videoPath;
		mCapture = capture;
		mTimeBetweenFrames = 1000/videoFrameRate;
		File tempFolder = new File(System.getProperty("java.io.tmpdir"), "ScreenRec");
		tempFolder.mkdir();
		mVideoEncoder = new CaptureScreenToFile(mVideoPath);
	}

	public void stop() {
		mVideoEncoder.setPauseTime(mCapture.getCurrentTime() - mDuration);
		mVideoEncoder.closeStreams();
	}

	public void run() {
		// update video duration
		mDuration += mTimeBetweenFrames;
		
		// capture frame
		mVideoEncoder.encodeImage(mVideoEncoder.takeSingleSnapshot());
		long seconds = mVideoEncoder.durationOfVideo();
		String timeOutput = GeneralUtils.convertToMinSecFormat(seconds);
		Call.workspace.timeDisplayLabel.setText(timeOutput);
	}
	
}
