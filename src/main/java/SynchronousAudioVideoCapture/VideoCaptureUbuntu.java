package SynchronousAudioVideoCapture;

import java.io.File;
import java.util.ArrayList;

import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.RandomStringUtils;

import gui.Call;

public class VideoCaptureUbuntu extends VideoCapture {
	
	private AvconvWrapper mAvconvWrapper;
	private int mFrameRate;
	private ArrayList<String> mTempFiles;
	private String mCurrentTempFile;
	private String mVideoPath;
	private SynchronousAudioVideoCapture mCapture;
	private boolean mIsBusy;
	private FFMPEGWrapper mFfmpegWrapper;
	
	public VideoCaptureUbuntu(String videoPath, int videoFrameRate, SynchronousAudioVideoCapture capture) {
		mVideoPath = videoPath;
		mFrameRate = videoFrameRate;
		mCapture = capture;
		mTempFiles = new ArrayList<String>();
		mCurrentTempFile = getNewTempFile("mp4");
		mIsBusy = false;
		mFfmpegWrapper = new FFMPEGWrapper();
	}
	
	public String getNewTempFile(String extension) {
		return (new File(System.getProperty("java.io.tmpdir"), RandomStringUtils.getSystemTimeString() + "." + extension)).getAbsolutePath();
	}

	@Override
	public int getFrameRate() {
		return mFrameRate;
	}

	@Override
	public void setFrameRate(int frameRate) {
		mFrameRate = frameRate;
	}

	@Override
	public void stop() {
		mIsBusy = true;
		mFfmpegWrapper.stitchVideo(mTempFiles, getNewTempFile("txt"), mVideoPath);
		mIsBusy = false;
	}

	@Override
	public void run() {
		if(mCapture.getState() == SynchronousAudioVideoCapture.States.PAUSED) {
			// PAUSED
			System.out.println("videoCapture pausing");
			// cancel schedule for the next call
			mCapture.getVideoCaptureScheduler().cancel(true);
			// stop screen recording
			mAvconvWrapper.stopRecordingScreen();
			// add the current temp file name to array
			mTempFiles.add(mCurrentTempFile);
			// create a new temp file
			mCurrentTempFile = getNewTempFile("mp4");
			System.out.println("new temp file : " + mCurrentTempFile);
		} else {
			if(!mAvconvWrapper.isRecording()) {
				// not recording currently, so start recording
				mAvconvWrapper = new AvconvWrapper(new File(mCurrentTempFile).getPath(), Call.workspace.framerate, Call.workspace.x, Call.workspace.y, Call.workspace.recordingWidth, Call.workspace.recordingHeight);
				mAvconvWrapper.startRecordingScreen();
			}
		}
	}

	@Override
	public boolean isBusy() {
		return mIsBusy;
	}

}
