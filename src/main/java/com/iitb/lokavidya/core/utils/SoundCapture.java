package com.iitb.lokavidya.core.utils;

import gui.Call;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JTextField;


public class SoundCapture {
	final int bufSize = 16384;
	  String path,standard;
	  Capture capture = new Capture();

	  Playback playback = new Playback();

	  AudioInputStream audioInputStream;

	  JButton playB, captB;

	  JTextField textField;

	  String errStr;

	  double duration, seconds;
	  
	  long currentTime, pauseTime=0,startPauseTime=0,pauseFlag=1,startTime=0,pauseTempTime=0;

	  File file;

	  public SoundCapture(/*String savepath,*/String standardname) {
	      //path=savepath;
	      standard=standardname;
	  }

	  public void startRecording()
	  {
		  System.out.println("rescord sound");
	      capture.start();
	      System.out.println("finish sound");
	  }
	    public void stopRecording()
	  {
	      capture.stop();
	  }
	  public void startPlayback()
	  {
	      playback.start();
	  }
	    public void stopPlayback()
	  {
	      playback.stop();
	  }  
	  /**
	   * Write data to the OutputChannel.
	   */
	  public class Playback implements Runnable {

	    SourceDataLine line;

	    Thread thread;

	    public void start() {
	      errStr = null;
	      thread = new Thread(this);
	      thread.setName("Playback");
	      thread.start();
	    }

	    public void stop() {
	      thread = null;
	    }

	    private void shutDown(String message) {
	      if ((errStr = message) != null) {
	        System.err.println(errStr);
	      }
	      if (thread != null) {
	        thread = null;
	        captB.setEnabled(true);
	        playB.setText("Play");
	      }
	    }

	    public void run() {

	      // make sure we have something to play
	      if (audioInputStream == null) {
	        shutDown("No loaded audio to play back");
	        return;
	      }
	      // reset to the beginnning of the stream
	      try {
	        audioInputStream.reset();
	      } catch (Exception e) {
	        shutDown("Unable to reset the stream\n" + e);
	        return;
	      }

	      // get an AudioInputStream of the desired format for playback

	      AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
	      float rate = 44100.0f;
	      int channels = 2;
	      int frameSize = 4;
	      int sampleSize = 16;
	      boolean bigEndian = true;

	      AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
	          * channels, rate, bigEndian);

	      AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format,
	          audioInputStream);

	      if (playbackInputStream == null) {
	        shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
	        return;
	      }

	      // define the required attributes for our line,
	      // and make sure a compatible line is supported.

	      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	      if (!AudioSystem.isLineSupported(info)) {
	        shutDown("Line matching " + info + " not supported.");
	        return;
	      }

	      // get and open the source data line for playback.

	      try {
	        line = (SourceDataLine) AudioSystem.getLine(info);
	        line.open(format, bufSize);
	      } catch (LineUnavailableException ex) {
	        shutDown("Unable to open the line: " + ex);
	        return;
	      }

	      // play back the captured audio data

	      int frameSizeInBytes = format.getFrameSize();
	      int bufferLengthInFrames = line.getBufferSize() / 8;
	      int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
	      byte[] data = new byte[bufferLengthInBytes];
	      int numBytesRead = 0;

	      // start the source data line
	      line.start();

	      while (thread != null) {
	        try {
	          if ((numBytesRead = playbackInputStream.read(data)) == -1) {
	            break;
	          }
	          int numBytesRemaining = numBytesRead;
	          while (numBytesRemaining > 0) {
	            numBytesRemaining -= line.write(data, 0, numBytesRemaining);
	          }
	        } catch (Exception e) {
	          shutDown("Error during playback: " + e);
	          break;
	        }
	      }
	      // we reached the end of the stream.
	      // let the data play out, then
	      // stop and close the line.
	      if (thread != null) {
	        line.drain();
	      }
	      line.stop();
	      line.close();
	      line = null;
	      shutDown(null);
	    }
	  } // End class Playback

	  /**
	   * Reads data from the input channel and writes to the output stream
	   */
	  class Capture implements Runnable {

	    TargetDataLine line;

	    Thread thread;

	    public void start() {
	      errStr = null;
	      thread = new Thread(this);

	      thread.setName("Capture");
	      System.out.println("in SoundCapture threaf start..");

	      //thread.setName("Capture");

	      thread.start();
	    }

	    public void stop() {
	      thread = null;
	    }

	    private void shutDown(String message) {
	      if ((errStr = message) != null && thread != null) {
	        thread = null;
	        playB.setEnabled(true);
	        captB.setText("Record");
	        System.err.println(errStr);
	      }
	    }

	    public void run() {

	    	
	      duration = 0;
	      audioInputStream = null;

	      // define the required attributes for our line,
	      // and make sure a compatible line is supported.
	      System.out.println("in SoundCapture threaf..");
	      AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
	      float rate = 44100.0f;
	      int channels = 2;
	      int frameSize = 4;
	      int sampleSize = 16;
	      boolean bigEndian = true;

	      AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
	          * channels, rate, bigEndian);
	      System.out.println("fromat created");
	      DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	      System.out.println("info created");

	      if (!AudioSystem.isLineSupported(info)) {
	        shutDown("Line matching " + info + " not supported.");
	        return;
	      }
	      System.out.println("line supporeted ");

	      // get and open the target data line for capture.

	      try {
	        line = (TargetDataLine) AudioSystem.getLine(info);
	        line.open(format, line.getBufferSize());
	      } catch (LineUnavailableException ex) {
	        shutDown("Unable to open the line: " + ex);
	        return;
	      } catch (SecurityException ex) {
	        shutDown(ex.toString());
	        //JavaSound.showInfoDialog();
	        return;
	      } catch (Exception ex) {
	        shutDown(ex.toString());
	        return;
	      }

	      // play back the captured audio data
	      ByteArrayOutputStream out = new ByteArrayOutputStream();
	      ByteArrayOutputStream out1 = new ByteArrayOutputStream();
	      int frameSizeInBytes = format.getFrameSize();
	      int bufferLengthInFrames = line.getBufferSize() / 8;
	      int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
	      byte[] data = new byte[bufferLengthInBytes];
	      int numBytesRead;
	      System.out.println("before line start()");
	      line.start();
	      //System.out.println("survived");
	      startTime = System.currentTimeMillis();
	      
	      while (thread != null) {
	    	  if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
	    		  System.out.println("Breaking from while ntz");
					break;
				}  
	        if (!Call.workspace.paused){ 
	        	if(pauseFlag==0){
	        		pauseTime = pauseTime+pauseTempTime;
	        		pauseTempTime=0;
	        		pauseFlag=1;
	        	}
	        	currentTime = System.currentTimeMillis() - startTime;
	        	currentTime = currentTime - pauseTime;
	        	System.out.println("Sound Time: "+currentTime/1000);
	        	if(!Call.workspace.screenRecordingFlag){
	        	String Display = GeneralUtils.convertToMinSecFormat(currentTime/1000);
	        	Call.workspace.timeDisplayLabel.setText(Display);
	        	}
				out.write(data, 0, numBytesRead);
	        }
	        else{
	        	if(pauseFlag==1){
	        		startPauseTime = System.currentTimeMillis();
	        		pauseFlag=0;
	        	}
	        	pauseTempTime = System.currentTimeMillis() - startPauseTime;
	        	out1.write(data, 0, numBytesRead);
	        	//System.out.println("Paused!");
	        }
	      }
	      System.out.println("out of while");

	      // we reached the end of the stream.
	      // stop and close the line.
	      line.stop();
	      line.close();
	      line = null;
	      System.out.println("reached here");
	      // stop and close the output stream
	      try {
	        out.flush();
	        out.close();
	        out1.flush();
	        out1.close();
	      } catch (IOException ex) {
	        ex.printStackTrace();
	      }

	      // load bytes into the audio input stream for playback

	      byte audioBytes[] = out.toByteArray();
	      ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
	      audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);
	      try {

	    	  System.out.println("in  .wav creation");
	    	  
	    	  System.out.println("Saving at "+standard);
	    	 
		         
	      AudioSystem.write(audioInputStream,AudioFileFormat.Type.WAVE,new File(standard));

	    

	      long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format
	          .getFrameRate());
	      duration = milliseconds / 1000.0;

	      
	        audioInputStream.reset();
	      } catch (Exception ex) {
	        ex.printStackTrace();
	        return;
	      }

	    }
	  } // End class Capture

	  public static void main(String args[]) {
		
	    
	  }
}


