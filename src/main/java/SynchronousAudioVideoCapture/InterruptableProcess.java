package SynchronousAudioVideoCapture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class InterruptableProcess {
	
	class StreamGobbler extends Thread {
		InputStream is;
		String type;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					System.out.println(type + ">" + line);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	private Process mProcess;
	private Runtime mRuntime;
	private String[] mCommand;
	private boolean mIsProcessRunning;
	
	public InterruptableProcess(String[] command) {
		mRuntime = Runtime.getRuntime();
		mCommand = command;
		mIsProcessRunning = false;
	}
	
	public boolean start() {
		try {
			mProcess = mRuntime.exec(mCommand);
			System.out.print("running the following command : ");
			for(String s: mCommand) { 
				System.out.print(s + " ");
			} System.out.println();
			StreamGobbler errorGobbler = new StreamGobbler(mProcess.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(mProcess.getInputStream(), "OUTPUT");
			errorGobbler.start();
			outputGobbler.start();
			return mIsProcessRunning = true;
		} catch (IOException e) {
			e.printStackTrace();
		} return false;
	}

	public boolean stop(){
		if(mIsProcessRunning) {
			try{
				mProcess.destroy();
				return true;
			}catch(Exception e){
				e.printStackTrace();
			} return false;	
		} else {
			return true;
		}
	}
	
	public static void main(String[] args) {
		String[] command = {"which", "avconv"};
		InterruptableProcess p = new InterruptableProcess(command);
		System.out.println("process started : " + p.start());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("process destroyed : " + p.stop());
	}
}
