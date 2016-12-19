package Xuggler;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.GeneralUtils;

import gui.Call;

public class VideoCapture implements Runnable {
	long pauseSeconds = 0, pauseTempSeconds = 0, pauseFlag = 1, pauseStart = 0;
	Thread thread;
	String outFile;
	CaptureScreenToFile videoEncoder;

	public VideoCapture() {

	}

	public void addFile(String x) {
		outFile = x;
	}

	public void start() {

		thread = new Thread(this);
		if (thread == null) {
			System.out.println("Thread starting off as null");
			// thread.setName("Capture");
		} else {
			thread.start();
		}
	}

	public void stop() {
		thread = null;
		System.out.println("Stopping thread");
	}

	private void shutDown(String message) {
		if (message != null && thread != null) {

			thread = null;

			System.err.println(message);
		}
	}

	public void run() {
		try {
			File tempFolder = new File(System.getProperty("java.io.tmpdir"), "ScreenRec");
			tempFolder.mkdir();
			videoEncoder = new CaptureScreenToFile(outFile);

			while (thread != null) {
				if (!Call.workspace.paused) {
					// System.out.println("encoded image");
					/*
					 * File outputfile; int ctr=1; try{ String
					 * no=Integer.toString(ctr); while(no.length()<4) {
					 * no="0"+no; }
					 * 
					 * outputfile = new
					 * File(tempFolder.getAbsolutePath(),("img"+no+".png"));
					 * ImageIO.write(videoEncoder.takeSingleSnapshot(), "png",
					 * outputfile); } catch (IOException e) {
					 * e.printStackTrace(); }
					 */
					if (pauseFlag == 0) {
						pauseFlag = 1;
						pauseSeconds = pauseSeconds + pauseTempSeconds;
						videoEncoder.setPauseTime(pauseSeconds);
						pauseTempSeconds = 0;
					}
					videoEncoder.encodeImage(videoEncoder.takeSingleSnapshot());
					long seconds = videoEncoder.durationOfVideo();
					System.out.println("Video Time:" + seconds);
					/*
					 * long mins=0; mins = seconds/60; seconds = seconds%60;
					 * String timeOutput=""; if(mins<10){
					 * System.out.print("0"+mins+":"); timeOutput =
					 * timeOutput+"0"+mins+":"; } else{
					 * System.out.print(""+mins+":"); timeOutput =
					 * timeOutput+mins+":"; } if(seconds<10){
					 * System.out.print("0"+seconds); timeOutput =
					 * timeOutput+"0"+seconds; } else{
					 * System.out.print(""+seconds); timeOutput =
					 * timeOutput+seconds; }
					 */
					String timeOutput = GeneralUtils.convertToMinSecFormat(seconds);
					Call.workspace.timeDisplayLabel.setText(timeOutput);
					// sleep for framerate milliseconds
					Thread.sleep(videoEncoder.getDuration());
				} else {
					if (pauseFlag == 1) {
						pauseStart = System.currentTimeMillis();
						pauseFlag = 0;
					} else
						pauseTempSeconds = (System.currentTimeMillis() - pauseStart);

					// System.out.println("Paused seconds: "+pauseTempSeconds+"
					// "+pauseStart+ " ");
				}

			}
			videoEncoder.closeStreams();
			// new FFMPEGWrapper().encodeImages(tempFolder.getAbsolutePath(),
			// outFile,Integer.toString(videoEncoder.getFps()));
		} catch (Exception e) {
			System.out.println("RunTime Exception");
			shutDown(e.getMessage());
		}
	}
}