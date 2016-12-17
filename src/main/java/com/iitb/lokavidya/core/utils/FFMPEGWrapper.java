package com.iitb.lokavidya.core.utils;

import gui.Call;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;


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

public class FFMPEGWrapper {

	public String pathExecutable;
	public static String pathExecutableffprobe;
	public static String encoding;

	public static void main(String args[]) {
		FFMPEGWrapper wrapper = new FFMPEGWrapper();
		/*
		 * try { wrapper.convertWavToMp3("/home/frg/Documents/sample1.wav",
		 * "/home/frg/Documents/sample1.mp3");
		 * 
		 * } catch (IOException | InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		ArrayList<String> vList = new ArrayList<String>();

		vList.add("C:\\Users\\hp\\Documents\\RainWaterTank-Marathi\\goqm1hgnpa.wav");
		vList.add("C:\\Users\\hp\\Documents\\RainWaterTank-Marathi\\n6xhgpxwfy.wav");

		wrapper.standardizeResolution(vList);
		wrapper.standardizeFps(vList);
		try {
			wrapper.stitchVideo(vList, "C:\\Users\\hp\\Documents\\temp.txt",
					"C:\\Users\\hp\\Documents\\join.wav");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FFMPEGWrapper() {
		String osname = System.getProperty("os.name");
		System.out.println(osname);

		pathExecutable = new File(new File(new File("lib").getAbsolutePath(), "ffmpeg").getAbsolutePath(), "bin").getAbsolutePath();
		pathExecutableffprobe = pathExecutable;
		encoding = "libx264";
		
		String osPathString;

		if (osname.contains("Windows")) {
			System.out.println("Setting to Windows");
			osPathString = "windows";
		}
		else if(osname.toLowerCase().contains("mac")){

			System.out.println("Setting to Mac");
			osPathString = "macosx";
			// set encoding to libx264, only for mac
			encoding = "libx264";
		}
		else {
			if (System.getProperty("sun.arch.data.model").contains("32")) {
				System.out.println("Setting to Linux 32");
				osPathString = "linux32";
			} else {
				System.out.println("Setting to linux 64");
				osPathString = "linux64";
			}
		}
		
		pathExecutable = new File(new File(pathExecutable, osPathString).getAbsolutePath(), "ffmpeg").getAbsolutePath();
		pathExecutableffprobe = new File(new File(pathExecutableffprobe, osPathString).getAbsolutePath(), "ffprobe").getAbsolutePath();
		System.out.println("ffmpeg pathExecutable : " + pathExecutable);
		System.out.println("ffprobe pathExecutable : " + pathExecutableffprobe);
	}

	public  boolean encodeImages(String folderPath, String tempPath,String fps)
	{
		//ffmpeg -framerate 1/5 -i img%03d.png -c:v libx264 -r 30 -pix_fmt yuv420p out.mp4
		String imgPath=new File(folderPath,"img%03d.png").getAbsolutePath();
		String[] command = new String[] {
		pathExecutable, "-framerate", fps, "-i", imgPath, "-c:v", encoding, "-r",
		"30", "-pix_fmt", "yuv420p", "-vf", "scale=320:240", tempPath };
				
		return GeneralUtils.runProcess(command);
	}
	
	public String getPreview(String VideoURL){
		String imgUrl;
		String parentPath=new File(VideoURL).getParent();
		imgUrl=new File(parentPath,(FilenameUtils.getBaseName(VideoURL)+".png")).getAbsolutePath();
		if (!new File(imgUrl).exists()) {
			String[] command = new String[] { pathExecutable, "-i", VideoURL, "-ss", "00:00:03", "-vframes", "1",
					imgUrl };
			GeneralUtils.runProcess(command);
			System.out.println("Finished running process");
		}
		return imgUrl;
		
	}
	
	//ffmpeg -loop 1 -i img.jpg -i audio.wav -c:v libx264 -c:a aac -strict experimental -b:a 192k -shortest out.mp4
	public boolean stitchImageAndAudio(String imgPath, String audioPath,
			String videoPath) throws IOException,
			InterruptedException {
		/*
		 * Check if final or temp exists delete if it does.
		 */
		System.out.println("stitching audio");
		
		File video = new File(videoPath);
		if (video.exists())
			video.delete();
		
		String resolution = "scale=-1:" + Call.workspace.videoHeight + ",pad=" + Call.workspace.getWidth() + ":ih:(ow-iw)/2";
		String[] command = new String[] {
				pathExecutable, "-loop", "1", "-i", imgPath,"-i",audioPath,"-c:v", encoding, "-c:a",
						"aac", "-strict","experimental","-pix_fmt", "yuv420p", "-vf", resolution, "-b:a", "192k","-shortest",videoPath };
				
		return	GeneralUtils.runProcess(command);
	}	

	public boolean stitchVideo(List<String> videoPaths, String listfilePath,
			String finalPath) throws IOException, InterruptedException {
		
		System.out.println("its starting");
		for(int i=0; i<videoPaths.size(); i++) {
			System.out.println(videoPaths.get(i));
		}
		
		String path = "";
		// re-encode the videos to libx264 codec
		// ffmpeg -i input1.mp4 -c:v libx264 -preset slow -crf 22 -c:a copy output1.mp4
		// ffmpeg -i input2.mp4 -c:v libx264 -preset slow -crf 22 -c:a copy output2.mp4
		ArrayList<String> libx264_filenames = new ArrayList<String>();
		for(int i=0; i<videoPaths.size(); i++) {
			String videoPath = videoPaths.get(i);
			String videoFileName = FilenameUtils.getBaseName(videoPath);
			String extension = FilenameUtils.getExtension(videoPath);
			
			path = videoPath.substring(0, videoPath.length() - videoFileName.length() - extension.length() - 1);
			String libx264_fileName = new File(path, videoFileName + "_264.mp4").getAbsolutePath();
			
			String[] command = new String[] {
				pathExecutable,
				"-y",
				"-i",
				videoPath,
				"-c:v",
				"libx264",
				"-preset",
				"slow",
				"-crf",
				"22",
				"-c:a",
				"copy",
				libx264_fileName
			};
			for(int j=0; j<command.length; j++) {
				System.out.print(command[j] + " ");
			}
			GeneralUtils.runProcess(command);
			libx264_filenames.add(libx264_fileName);
		}
		
		// convert the libx264 encoded videos to intermediate mpg videos
		// ffmpeg -i output1.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts inter1.ts
		// ffmpeg -i output2.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts inter2.ts
		ArrayList<String> interFilesList = new ArrayList<String>();
		for(int i=0; i<libx264_filenames.size(); i++) {
			String interFile = new File(path, "inter" + i + ".ts").getAbsolutePath();
			String[] command = new String[] {
					pathExecutable,
					"-y",
					"-i",
					libx264_filenames.get(i),
					"-c",
					"copy",
					"-bsf:v",
					"h264_mp4toannexb",
					"-f",
					"mpegts",
					interFile
			};
			for(int j=0; j<command.length; j++) {
				System.out.print(command[j] + " ");
			}
			GeneralUtils.runProcess(command);
			interFilesList.add(interFile);
		}
		
		// concatenate the two videos to generate output video
		// ffmpeg -i "concat:inter1.ts|inter2.ts" -c copy -bsf:a aac_adtstoasc output.mp4		
		
		String osName = System.getProperty("os.name");
		String concatFilesString;
		if(osName.contains("Windows")) {
			concatFilesString = "\"concat:";
			for(int i=0; i<interFilesList.size(); i++) {
				concatFilesString += interFilesList.get(i) + "|";
			}
			
			String osname = System.getProperty("os.name");
			if(osname .contains("Windows")) {
				
			}
			concatFilesString = concatFilesString.substring(0, concatFilesString.length()-1);
			concatFilesString += "\"";
		} else {
			concatFilesString = "concat:";
			for(int i=0; i<interFilesList.size(); i++) {
				concatFilesString += interFilesList.get(i) + "|";
			}
			
			String osname = System.getProperty("os.name");
			if(osname .contains("Windows")) {
				
			}
			concatFilesString = concatFilesString.substring(0, concatFilesString.length()-1);
			concatFilesString += "";
		}
		
		String[] command = new String[] {
				pathExecutable,
				"-y",
				"-i",
				concatFilesString,
				"-c:v",
				encoding,
				"-bsf:a",
				"aac_adtstoasc",
				"-qscale",
				"1",
				finalPath
		};
		for(int j=0; j<command.length; j++) {
			System.out.print(command[j] + " ");
		}
		GeneralUtils.runProcess(command);
		
		// delete intermediate files
		for(int i=0; i<libx264_filenames.size(); i++) {
			new File(libx264_filenames.get(i)).delete();
		}
		
		for(int i=0; i<interFilesList.size(); i++) {
			new File(interFilesList.get(i)).delete();
		}
		
		// Writing files to order.
//		System.out.println(listfilePath);
//		System.out.println(finalPath);
//		File file = new File(listfilePath);
//		if (file.exists())
//			file.delete();
//		File orderVideoFile = new File(listfilePath);
//		try {
//			FileOutputStream out = new FileOutputStream(orderVideoFile);
//			for (int i = 0; i < videoPaths.size(); i++) {
//				String data = "file '" + videoPaths.get(i) + "'\n\r";
//				out.write(data.getBytes());
//				System.out.println(data);
//			}
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		
//		String[] command = new String[] { pathExecutable, "-f", "concat","-safe","0",
//				"-i", orderVideoFile.getAbsolutePath(), "-codec", "copy",
//				finalPath };
//		file = new File(finalPath);
//		System.out.println(orderVideoFile.getAbsolutePath());
//		if (file.exists())
//			file.delete();
//		String cmd = pathExecutable;// +" -help";
//		String cmdext = " -f concat -i " + orderVideoFile.getAbsolutePath()
//				+ " -codec copy  " + finalPath + "";
//		cmd += cmdext;
//		GeneralUtils.runProcess(command);
		System.out.println("stitch done.. ");

		return true;

	}

	public boolean processVideo(String videoPath) {
		String tmpPath = System.getProperty("java.io.tmpdir");
		System.out.println("Video Path: " + videoPath);
		String tempAudioOutput = new File(tmpPath, "tempAudio.mp3")
				.getAbsolutePath();
		System.out.println("Temp output: " + tempAudioOutput);

		File file = new File(tempAudioOutput);
		if (file.exists())
			file.delete();

		String[] commandAudio = new String[] { pathExecutable, "-i", videoPath,
				tempAudioOutput };
		GeneralUtils.runProcess(commandAudio);
		String tempVideoOutput = new File(tmpPath, "tempVideo.mp4")
				.getAbsolutePath();
		file = new File(tempVideoOutput);
		if (file.exists())
			file.delete();
		String[] commandVideo = new String[] { pathExecutable, "-i", videoPath,
				"-vcodec", "copy", "-an", tempVideoOutput };
		GeneralUtils.runProcess(commandVideo);
		String[] commandVideoCombine = new String[] { pathExecutable, "-i",
				tempAudioOutput, "-i", tempVideoOutput,"-ar","44100",
				"-vcodec", "copy", "-strict", "-2", videoPath };
		file = new File(videoPath);
		if (file.exists())
			file.delete();
		System.out.println("Video Combine part command: "
				+ commandVideoCombine);
		GeneralUtils.runProcess(commandVideoCombine);
		return true;
	}

	public boolean standardizeResolution(List<String> videoPaths) {
		for (String vPath : videoPaths) {
			String cmd = " " + pathExecutable;// +" -help";
			String cmdext = " -i " + vPath
					+ " -s 720x480 -b 512k -vcodec copy -acodec copy";
			cmd += cmdext;
			Runtime run = Runtime.getRuntime();
			Process pr;
			try {
				pr = run.exec(cmd);
				pr.waitFor();
				BufferedReader buf = new BufferedReader(new InputStreamReader(
						pr.getInputStream()));
				String line = "";
				while ((line = buf.readLine()) != null) {
					System.out.println(line);
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// ffmpeg -i <inputfilename> -s 640x480 -b 512k -vcodec mpeg1video
		// -acodec copy
		return true;
	}

	public boolean standardizeResolutionMaintainAspectRatio(String videoURL,String projectURL, String videoName) {

		System.out.println("resizing... ");
		System.out.println("video path: " + videoURL);
		String outputPath = new File(projectURL, videoName + "."
				+ FilenameUtils.getExtension(videoURL)).getAbsolutePath();
		
		String resolution=
				"[in]scale=iw*min("+
				Integer.toString(Call.workspace.videoWidth)+"/iw\\,"+
				Integer.toString(Call.workspace.videoHeight)+
				"/ih):ih*min("+Integer.toString(Call.workspace.videoWidth)+
				"/iw\\,"+
				Integer.toString(Call.workspace.videoHeight)+
				"/ih)[scaled]; [scaled]pad="+
				Integer.toString(Call.workspace.videoWidth)+
				":"+Integer.toString(Call.workspace.videoHeight)+
				":("+Integer.toString(Call.workspace.videoWidth)+
				"-iw*min("+Integer.toString(Call.workspace.videoWidth)+
				"/iw\\,"+Integer.toString(Call.workspace.videoHeight)+
				"/ih))/2:("+Integer.toString(Call.workspace.videoHeight)+
				"-ih*min("+Integer.toString(Call.workspace.videoWidth)+
				"/iw\\,"+Integer.toString(Call.workspace.videoHeight)+
				"/ih))/2[padded]; [padded]setsar=1:1[out]";
		
		String[] command = new String[] {
				pathExecutable,
				"-y",
				"-i",
				videoURL,
				"-vf",
				resolution,
				"-c:v", 
				encoding, 
				"-c:a", 
				"copy", 
				outputPath 
		};

		// print the command
		for(int i=0; i<command.length; i++) {
			System.out.print(command[i] + " ");
		} System.out.println();
		
		return GeneralUtils.runProcess(command);

	}
	
	public boolean standardiseResolutionMaintainAspectRatioAndProcessVideo(String videoURL,String outputVideoURL) {
		
		// extract audio from the input video file
		String tmpPath = System.getProperty("java.io.tmpdir");
		System.out.println("Video Path: " + videoURL);
		String tempAudioOutput = new File(tmpPath, "tempAudio.mp3")
				.getAbsolutePath();
		System.out.println("Temp output: " + tempAudioOutput);

		File file = new File(tempAudioOutput);
		if (file.exists())
			file.delete();

		String[] commandAudio = new String[] { pathExecutable, "-i", videoURL, tempAudioOutput };
		GeneralUtils.runProcess(commandAudio);
		
		// extract video from the input video file
		String tempVideoOutput = new File(tmpPath, "tempVideo.mp4")
				.getAbsolutePath();
		file = new File(tempVideoOutput);
		if (file.exists())
			file.delete();
		String[] commandVideo = new String[] { pathExecutable, "-i", videoURL,
				"-vcodec", "copy", "-an", tempVideoOutput };
		GeneralUtils.runProcess(commandVideo);
		
		// delete existing video
		file = new File(videoURL);
		if (file.exists())
			file.delete();
		
		// combine audio and video
		String resolution=
				"[in]scale=iw*min("+
				Integer.toString(Call.workspace.videoWidth)+"/iw\\,"+
				Integer.toString(Call.workspace.videoHeight)+
				"/ih):ih*min("+Integer.toString(Call.workspace.videoWidth)+
				"/iw\\,"+
				Integer.toString(Call.workspace.videoHeight)+
				"/ih)[scaled]; [scaled]pad="+
				Integer.toString(Call.workspace.videoWidth)+
				":"+Integer.toString(Call.workspace.videoHeight)+
				":("+Integer.toString(Call.workspace.videoWidth)+
				"-iw*min("+Integer.toString(Call.workspace.videoWidth)+
				"/iw\\,"+Integer.toString(Call.workspace.videoHeight)+
				"/ih))/2:("+Integer.toString(Call.workspace.videoHeight)+
				"-ih*min("+Integer.toString(Call.workspace.videoWidth)+
				"/iw\\,"+Integer.toString(Call.workspace.videoHeight)+
				"/ih))/2[padded]; [padded]setsar=1:1[out]";
		
		String[] commandVideoCombine = new String[] { 
			pathExecutable, 
			"-i",
			tempAudioOutput, 
			"-i", 
			tempVideoOutput, 
			"-vf", 
			resolution, 
			"-ar",
			"44100",
			"-c:v", 
			encoding, 
			"-strict", 
			"-2", 
			outputVideoURL 
		};
		
		System.out.println("Video Combine part command: "
				+ commandVideoCombine);
		GeneralUtils.runProcess(commandVideoCombine);
		return true;
		
	}

	public boolean standardizeFps(List<String> videoPaths) {
		for (String vPath : videoPaths) {
			String cmd = " " + pathExecutable;// +" -help";
			File f1 = new File(new File(vPath).getParent(), "temp.mp4");
			String cmdext = " -i " + vPath + " -sameq -r 4 -y "
					+ f1.getAbsolutePath();
			cmd += cmdext;
			Runtime run = Runtime.getRuntime();
			Process pr;
			try {

				pr = run.exec(cmd);
				pr.waitFor();
				BufferedReader buf = new BufferedReader(new InputStreamReader(
						pr.getInputStream()));
				String line = "";
				while ((line = buf.readLine()) != null) {
					System.out.println(line);
					new File(vPath).delete();
					f1.renameTo(new File(vPath));
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean convertMp3ToWav(String mp3Path, String wavPath) {
		// ffmpeg -i 111.mp3 -acodec pcm_s16le -ac 1 -ar 16000 out.wav
		String[] command = new String[] { pathExecutable, "-i", mp3Path,
				"-acodec", "pcm_s16le", "-ac", "1", "-ar", "16000", wavPath };
		File file = new File(wavPath);
		if (file.exists())
			file.delete();
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(command);
			pr.waitFor();
			StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream(),
					"ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(
					pr.getInputStream(), "OUTPUT");
			errorGobbler.start();
			outputGobbler.start();
			int exitVal = pr.waitFor();
			System.out.println("ExitValue: " + exitVal);
			System.out.println("Wait for has ended ");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean convertWavToMp3(String wavPath, String mp3Path)
			throws IOException, InterruptedException {
		String cmd = " " + pathExecutable;// +" -help";
		System.out.println("WAV PATH: " + wavPath);
		System.out.println("mp3PATH: " + mp3Path);
		String cmdext = " -i " + wavPath + " -f mp2 " + mp3Path;
		cmd += cmdext;
		String[] command = new String[] { pathExecutable, "-i", wavPath, "-f",
				"mp2", mp3Path };
		File file = new File(mp3Path);
		if (file.exists()) {
			file.delete();

		}
		System.out.println("Audio conversion command: " + command.toString()
				+ "\n" + cmd);
		return GeneralUtils.runProcess(command);
	}
    public long getDuration(String filePath){
    	String[] command = new String[] { pathExecutableffprobe, "-v", "error", "-show_entries",
				"format=duration","-of", "default=noprint_wrappers=1:nokey=1", filePath };
    	System.out.println("Inside FFmpeg:"+pathExecutableffprobe);
    	return GeneralUtils.runProbe(command);
    	
    }
	
}
