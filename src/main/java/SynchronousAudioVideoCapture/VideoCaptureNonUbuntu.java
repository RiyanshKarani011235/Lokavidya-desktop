package SynchronousAudioVideoCapture;

import java.io.File;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import Xuggler.CaptureScreenToFile;
import Xuggler.CaptureScreenToFileMark2;
import gui.Call;

public class VideoCaptureNonUbuntu extends VideoCapture {
	private String mVideoPath;
	private CaptureScreenToFileMark2 mVideoEncoder;
	private int mTimeBetweenFrames;
	private SynchronousAudioVideoCapture mCapture;
	private long mDuration;
	private long mTimeOfPreviousSnapshotCapture;

	public VideoCaptureNonUbuntu(String videoPath, int videoFrameRate, SynchronousAudioVideoCapture capture) {
		mVideoPath = videoPath;
		mCapture = capture;
		mTimeBetweenFrames = 1000/videoFrameRate;
		File tempFolder = new File(System.getProperty("java.io.tmpdir"), "ScreenRec");
		tempFolder.mkdir();
		mVideoEncoder = new CaptureScreenToFileMark2(mVideoPath);
	}
	
	@Override
	public int getFrameRate() {
		return (int) 1000/mTimeBetweenFrames;
	}
	
	@Override
	public void setFrameRate(int videoFrameRate) {
		mTimeBetweenFrames = 1000/videoFrameRate;
	}

	@Override
	public void stop() {
		while(mVideoEncoder.getState() == CaptureScreenToFileMark2.State.RUNNING);
		mVideoEncoder.setPauseTime(mCapture.getCurrentTime() - mDuration);
		mVideoEncoder.closeStreams();
	}

	@Override
	public void run() {
		System.out.println("video capture run called");
		long previousSnapshotCaptureTime = mTimeOfPreviousSnapshotCapture;
		long currentTime = System.currentTimeMillis();
		if(previousSnapshotCaptureTime != -1) {
			double framrate = 1000.0 / (currentTime - previousSnapshotCaptureTime);
			System.out.println("current framrate : " + framrate + "frames per second");
		} 
		mTimeOfPreviousSnapshotCapture = currentTime;
		
		// update video duration
		mDuration += mTimeBetweenFrames;
		
		// capture frame
		mVideoEncoder.takeSingleSnapshot();
		
		// if paused, then cancel schedule for the next call
		if(mCapture.getState() == SynchronousAudioVideoCapture.States.PAUSED) {
			System.out.println("videoCapture pausing");
			mCapture.getVideoCaptureScheduler().cancel(true);
		}
	}
	
	@Override
	public boolean isBusy() {
		if(mVideoEncoder.getState() == CaptureScreenToFileMark2.State.RUNNING) {
			return true;
		} return false;
	}
	
}
