package SynchronousAudioVideoCapture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
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

public class SynchronousAudioVideoCapture {
	
	// constants
	static int DEFAULT_AUDIO_FRAME_RATE = 100;
	static int DEFAULT_VIDEO_FRAME_RATE = Call.workspace.framerate;

	// variables
	private ScheduledThreadPoolExecutor mExecutor;
	
	private ScheduledFuture mAudioCaptureScheduler;
	private ScheduledFuture mVideoCaptureScheduler;
	private ScheduledFuture mOtherTasksScheduler;
	private ScheduledFuture mPauseObserverScheduler;
	
	private AudioCapture mAudioCapture;
	private VideoCapture mVideoCapture;
	private OtherTasks mOtherTasks;
	private StateObserver mStateobserver;
	
	private long mCurrentTime;
	
	public long getCurrentTime() {
		return mCurrentTime;
	}
	
	public static enum States {
		RUNNING,
		PAUSED,
		STOPPED
	}
	
	public ScheduledFuture getAudioCaptureScheduler() {
		return mAudioCaptureScheduler;
	}
	
	public ScheduledFuture getVideoCaptureScheduler() {
		return mVideoCaptureScheduler;
	}
	
	/* 
	 * STATE MACHINE IMPLEMENTATION
	 */
	private States mState;
	
	public synchronized States accessState(boolean changeState, States newState) {
		if(changeState) {
			mState = newState;
		} return mState;
	}
	
	public synchronized States getState() {
		return accessState(false, null);
	}
	
	public synchronized void setState(States state) {
		accessState(true, state);
	}
	
	public class StateObserver implements Runnable {
		// monitor pause and run states 
		public void run() {
			if(getState() == States.STOPPED) {
				// STOPPED, DON'T DO ANYTHING
			} else {
				if(getState() == States.RUNNING) {
					// RUNNING
					if(Call.workspace.paused) {
						// PAUSE RECORDING
						pauseRecording();
					}
				} else {
					// PAUSED
					if(!Call.workspace.paused) {
						// RESUME RECORDING
						resumeRecording();
					}
				}
			}
		}
	}
	
	public class OtherTasks implements Runnable {
		
		public void run() {
			// update current time every second
			mCurrentTime += 1000;
			String Display = GeneralUtils.convertToMinSecFormat(mCurrentTime / 1000);
			Call.workspace.timeDisplayLabel.setText(Display);
			System.out.println("capture time : " + (mCurrentTime / 1000) + " seconds");
			
			if(getState() == SynchronousAudioVideoCapture.States.PAUSED) {
				System.out.println("audio capture pausing");
				mOtherTasksScheduler.cancel(true);
			}
		}
	}

	public SynchronousAudioVideoCapture(String audioFilePath, String videoFilePath) throws Exception {

		mCurrentTime = 0;
		
		mExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);
		
		// set initial state
		setState(States.STOPPED);
		
		mAudioCapture = new AudioCapture(audioFilePath, DEFAULT_AUDIO_FRAME_RATE, SynchronousAudioVideoCapture.this);
		mVideoCapture = VideoCaptureFactory.getVideoCapture(videoFilePath, DEFAULT_VIDEO_FRAME_RATE, SynchronousAudioVideoCapture.this);
		mOtherTasks = new OtherTasks();
		mStateobserver = new StateObserver();
	}
	
	private void cancelScheduler(ScheduledFuture scheduler) {
		if (scheduler != null) {
			// TODO check why scheduler.cancel not working
			if(scheduler.cancel(false)) {
				System.out.println("stopped scheduler : " + scheduler.toString());
			} else {
				System.out.println("could not stop scheduler scheduler : " + scheduler.toString());
			}
		}
	}
	
	public void resumeRecording() {
		// get the latest frame rate
		DEFAULT_VIDEO_FRAME_RATE = Call.workspace.framerate;
		mVideoCapture.setFrameRate(DEFAULT_VIDEO_FRAME_RATE);
		System.out.println("starting video recording at frame rate : " + mVideoCapture.getFrameRate());
		
		setState(States.RUNNING);
		mAudioCaptureScheduler = mExecutor.scheduleAtFixedRate(mAudioCapture, 0, (long) (1000 / mAudioCapture.getFrameRate()), TimeUnit.MILLISECONDS);
		mVideoCaptureScheduler = mExecutor.scheduleAtFixedRate(mVideoCapture, 0, (long) (1000 / mVideoCapture.getFrameRate()), TimeUnit.MILLISECONDS);
		mOtherTasksScheduler = mExecutor.scheduleAtFixedRate(mOtherTasks, 0, 1000, TimeUnit.MILLISECONDS);
	}
	
	public void startRecording() {
		resumeRecording();
		// start monitoring Workspace
		mPauseObserverScheduler = mExecutor.scheduleAtFixedRate(mStateobserver, 0, 10, TimeUnit.MILLISECONDS); // check for pause / record state every 10 milliseconds
	}
	
	public void pauseRecording() {
		setState(States.PAUSED);
		cancelScheduler(mAudioCaptureScheduler);
		cancelScheduler(mVideoCaptureScheduler);
		cancelScheduler(mOtherTasksScheduler);
	}
	
	public void stopRecording() {
		// pause recording only if currently recording
		if(getState() == States.RUNNING) {
			setState(States.STOPPED);
			cancelScheduler(mAudioCaptureScheduler);
			cancelScheduler(mVideoCaptureScheduler);
			cancelScheduler(mOtherTasksScheduler);
		} else {
			setState(States.STOPPED);
		}
		cancelScheduler(mPauseObserverScheduler);
		
		// call the stop methods of the Capture classes
		try {
			mAudioCapture.stop();
			mVideoCapture.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// wait for video capture stack to be empty
		System.out.println("wait for video capture stack to be empty");
		System.out.println(mVideoCapture.isBusy());
		while(mVideoCapture.isBusy());
		System.out.println("video capture stack is empty now");
	}

}
