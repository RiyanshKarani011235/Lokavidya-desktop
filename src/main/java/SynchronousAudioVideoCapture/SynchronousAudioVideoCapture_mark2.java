package SynchronousAudioVideoCapture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import gui.Call;

public class SynchronousAudioVideoCapture_mark2 {

	private ScheduledThreadPoolExecutor mExecutor;
	private ScheduledFuture mAudioCaptureScheduler;
	private ScheduledFuture mVideoCaptureScheduler;
	private ScheduledFuture mOtherTasksScheduler;
	private AudioCapture mAudioCapture;
	private VideoCapture mVideoCapture;
	private OtherTasks mOtherTasks;

	private double mAudioFrameRate;
	private double mVideoFrameRate;
	
	private long mCurrentTime;
	
	public long getCurrentTime() {
		return mCurrentTime;
	}
	
	public class OtherTasks implements Runnable {
		
		public void run() {
			// update current time every second
			mCurrentTime += 1000;
			if (!Call.workspace.screenRecordingFlag) {
				String Display = GeneralUtils.convertToMinSecFormat(mCurrentTime / 1000);
				Call.workspace.timeDisplayLabel.setText(Display);
			}
			System.out.println("capture time : " + (mCurrentTime / 1000) + " seconds");
		}
	}

	public SynchronousAudioVideoCapture_mark2(int audioFrameRate, int videoFrameRate, String audioFilePath, String videoFilePath) throws Exception {

		if (audioFrameRate > 1000 || videoFrameRate > 1000) {
			throw new Exception("frame rate too high, should not be greater than 1000");
		}

		mAudioFrameRate = audioFrameRate;
		mVideoFrameRate = videoFrameRate;
		mCurrentTime = 0;
		
		mExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);
		mAudioCapture = new AudioCapture(audioFilePath);
		mVideoCapture = new VideoCapture(videoFilePath, videoFrameRate, SynchronousAudioVideoCapture_mark2.this);
		mOtherTasks = new OtherTasks();
	}
	
	public SynchronousAudioVideoCapture_mark2(String audioFilePath, String videoFilePath) throws Exception {
		// TODO get predefined framerates
		this(100, 2, audioFilePath, videoFilePath);
	}

	private void run() {
		mAudioCaptureScheduler = mExecutor.scheduleAtFixedRate(mAudioCapture, 0, (long) (1000 / mAudioFrameRate), TimeUnit.MILLISECONDS);
		mVideoCaptureScheduler = mExecutor.scheduleAtFixedRate(mVideoCapture, 0, (long) (1000 / mVideoFrameRate), TimeUnit.MILLISECONDS);
		mOtherTasksScheduler = mExecutor.scheduleAtFixedRate(mOtherTasks, 0, 1000, TimeUnit.MILLISECONDS);
	}
	

	private void cancelAllSchedulers() {
		cancelScheduler(mAudioCaptureScheduler);
		cancelScheduler(mVideoCaptureScheduler);
		cancelScheduler(mOtherTasksScheduler);
	}

	private void cancelScheduler(ScheduledFuture scheduler) {
		if (scheduler != null) {
			scheduler.cancel(false);
		}
	}
	
	public void startRecording() {
		run();
	}
	
	public void pauseRecording() {
		cancelAllSchedulers();
	}
	
	public void stopRecording() {
		pauseRecording();
		// TODO call the stop methods of the Capture classes
		try {
			mAudioCapture.stop();
			mVideoCapture.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AudioCapture getAudioCapture() {
		return mAudioCapture;
	}
	
	public VideoCapture getVideoCapture() {
		return mVideoCapture;
	}

	public static void main(String[] arg) {

		SynchronousAudioVideoCapture_mark2 s;
		try {
			s = new SynchronousAudioVideoCapture_mark2(2, 1, "/Users/ironstein/desktop/test", "/Users/ironstein/desktop/test");
			s.run();
			Thread.sleep(10000);
			s.cancelAllSchedulers();
			System.out.println("shutdown");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
