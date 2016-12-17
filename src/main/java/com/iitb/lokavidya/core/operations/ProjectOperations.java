package com.iitb.lokavidya.core.operations;

import gui.Call;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import Xuggler.DecodeAndSaveAudioVideo;
import Xuggler.VidCapture;
import Xuggler.VideoCapture;

import com.iitb.lokavidya.core.data.Audio;
import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Video;
import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.GeneralUtils;
import com.iitb.lokavidya.core.utils.SoundCapture;
import com.sun.star.setup.CopyFileAction;

import Dialogs.SaveRecordingVideo;

public class ProjectOperations {
	static SoundCapture currentSound = null;
	static VideoCapture currentMuteVideo=null;
	static String tempAudioURL;
	static String tempVideoURL;
	static SoundCapture currentAudio=null;

	public static void stitch(Project project) {
		FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper();
		ArrayList<String> videoPaths = new ArrayList<String>();
		Iterator<Segment> iterator = project.getOrderedSegmentList().iterator();
		String tmpPath = System.getProperty("java.io.tmpdir");
		double divider = (double)60/(double)project.getOrderedSegmentList().size();
		int value =10;
		int i=0;
		while (iterator.hasNext()) {
			Segment segment = iterator.next();
			// TODO Check audio format before attempting convert
			if (segment.getSlide() != null){
			if (segment.getSlide().getAudio() != null) {
				try {
					String audioURL = segment.getSlide().getAudio()
							.getAudioURL();
					System.out.println("Audio URL: " + audioURL);
					String imageURL = segment.getSlide().getImageURL();
					System.out.println("Image URL: " + imageURL);
					/*String audioName = RandomStringUtils.randomAlphanumeric(10)
							.toLowerCase();
					String mp3Path = project.getProjectURL() + File.separator
							+ audioName + ".mp3";
					ffmpegWrapper.convertWavToMp3(audioURL, mp3Path);
					// Update the audio properties
					System.out.println("outside wrapper..");
					Audio tempAudio = new Audio(mp3Path,
							project.getProjectURL());
					segment.getSlide().setAudio(tempAudio);
					segment.getSlide().getAudio().setAudioProperties();
					System.out.println("audio conversion complete");
					System.out.println("audio format: "
							+ segment.getSlide().getAudio().getAudioFormat());
					*/
					System.out.println("Video creation... ");
					String tempPath = new File(tmpPath, "temp.mp4")
							.getAbsolutePath();
					System.out.println("Temp path: " + tempPath);
					Video video = new Video(project.getProjectURL());
					System.out.println("VIDEO URL: " + video.getVideoURL());
					ffmpegWrapper.stitchImageAndAudio(imageURL, segment
							.getSlide().getAudio().getAudioURL(),
							video.getVideoURL());
					video.setVideoProperties();
					segment.setVideo(video);
					
					System.out.println(video.getVideoFormat());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			} 
			if(segment.getVideo()!=null)
			{
				segment.getVideo().setVideoProperties();
				videoPaths.add(segment.getVideo().getVideoURL());
			}
			value = (int)(10+(double)(i+1)*(divider));
			Call.workspace.progressBar.setValue(value);
			i++;
		}
		System.out.println("Video list :");
		Iterator<String> iter = videoPaths.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		
		// Begin concatenation
		String finalPath = new File(project.getProjectURL(),
				project.getProjectName() + ".mp4").getAbsolutePath();
		try {
			ffmpegWrapper.stitchVideo(videoPaths, new File(
					"concat.txt").getAbsolutePath(), finalPath);
			Call.workspace.progressBar.setValue(80);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int j=0; j<videoPaths.size(); j++) {
			if(j == 0) {
				
			}
		}
	}
	
	static Audio globalAudio;
	static Segment segment;
	static Video globalVideo;

	public static void startAudioRecording(Project project, Segment segmentA) {
		Call.workspace.startOperation();
		SegmentService.deleteVideo(project, segmentA);
		SegmentService.deleteAudio(project, segmentA);
		segment=segmentA;
		globalAudio = new Audio(project.getProjectURL());
		currentSound = new SoundCapture(globalAudio.getAudioURL());
		currentSound.startRecording();
	}

	public void startToggleSlideRecording(Project project, Segment segmentA) {
		Call.workspace.startOperation();
		SegmentService.deleteVideo(project, segmentA);
		SegmentService.deleteAudio(project, segmentA);
		segment=segmentA;
		tempAudioURL=new File(project.getProjectURL(),(RandomStringUtils.randomAlphanumeric(10).toLowerCase()+".wav")).getAbsolutePath();
		tempVideoURL=new File((RandomStringUtils.randomAlphanumeric(10).toLowerCase()+".flv")).getAbsolutePath();
		currentAudio = new SoundCapture(tempAudioURL);		
		currentMuteVideo=new VideoCapture();
		currentMuteVideo.addFile(tempVideoURL);
		currentMuteVideo.start();
		currentAudio.startRecording();
	}

	public static void playSlideRecording() {

	}

	public static void stopPlaySlideRecording() {

	}

	public static void stopAudioRecording(Project project) {
		List<Segment> slist = project.getOrderedSegmentList();
		for(Segment s:slist){
			if(s.getSlide()!=null){
				if(s.getSlide().getAudio()!=null && s.getVideo()==null){
					System.out.println("Checking:"+s.getSlide().getAudio().getAudioURL());
					FFMPEGWrapper ffmpegwrapper = new FFMPEGWrapper();
					long duration = ffmpegwrapper.getDuration(s.getSlide().getAudio().getAudioURL());
					System.out.println("Stopping Duration: "+duration);
					s.setTime(duration);
				}
			}
		}
		Call.workspace.endOperation();
	}
	
	public static void stopAudioToggleRecording(){
		currentSound.stopRecording();
		segment.getSlide().setAudio(globalAudio);
	}

	public static void discardSlideRecording(Project p) {
		currentSound.stopRecording();
		SegmentService.deleteAudio(p, segment);
		Call.workspace.cancelOperation();
	}

	public void discardToggleSlideRecording(Project project) {
		currentAudio.stopRecording();
        currentMuteVideo.stop();
		SegmentService.deleteVideo(project,segment );
		SegmentService.deleteAudio(project, segment);
        Call.workspace.cancelOperation();
	}

	public void stopToggleSlideRecording(Project project) {
		currentAudio.stopRecording();
        currentMuteVideo.stop();
        segment.getSlide().setTempAudioURL(tempAudioURL);
        segment.getSlide().setTempMuteVideoURL(tempVideoURL);
        
       /* File f=new File(tempVideoURL);
        File tempVideo=new File(project.getProjectURL(),RandomStringUtils.randomAlphanumeric(10).toLowerCase()+".flv");
		System.out.println("Saving at "+f.getAbsolutePath());
		DecodeAndSaveAudioVideo.stitch(f.getAbsolutePath(),tempAudioURL,tempVideo.getAbsolutePath());
		
		DecodeAndSaveAudioVideo.convertFormat(tempVideo.getAbsolutePath(),globalVideo.getVideoURL());
		Video screenVideo = new Video(globalVideo.getVideoURL(), project.getProjectURL());
		
		SegmentService.addVideo(project, segment,screenVideo);
		FFMPEGWrapper ffmpegwrapper = new FFMPEGWrapper();
		long duration = ffmpegwrapper.getDuration(segment.getVideo().getVideoURL());
		segment.setTime(duration);
		
		//Call.workspace.deleteList.add(f);
		Call.workspace.deleteList.add(new File(tempAudioURL));
		Call.workspace.deleteList.add(tempVideo);
		Call.workspace.endOperation(); */
	}

	public static void stopSlideRecording(Project project) {
		
//		System.out.println("stopSlideRecording called");
//		SaveRecordingVideo.run(project);
		
		List<Segment> slist = project.getOrderedSegmentList();
		for(Segment s:slist){
			if(s.getSlide()!=null) {
				// consider only the silde that has been altered (slide recording)
				System.out.println("ironstein - I am iron man");
				if(s.getSlide().getTempAudioURL()!=null && s.getSlide().getTempMuteVideoURL()!=null){
					
					// saving temp video as an flv file in lokavidya folder
					File originalTempVideo = new File(s.getSlide().getTempMuteVideoURL());
					System.out.println("originalTempVideo : Saving at : " + originalTempVideo.getAbsolutePath());
					
					// stitching audio and video files, and generating a flv file as output
					File tempVideo = new File(project.getProjectURL(), RandomStringUtils.randomAlphanumeric(10).toLowerCase()+".flv");
					System.out.println("tempVideo : Saving at : " + tempVideo.getAbsolutePath());
					DecodeAndSaveAudioVideo.stitch(originalTempVideo.getAbsolutePath(),s.getSlide().getTempAudioURL(),tempVideo.getAbsolutePath());
			
					// converting flv file to mp4 file
					// new implementation - ironstein - 23-11-16
//					globalVideo = new Video(project.getProjectURL());
//					System.out.println("globalVideo : url : " + globalVideo.getVideoURL());
//					DecodeAndSaveAudioVideo.convertFormat(tempVideo.getAbsolutePath(),globalVideo.getVideoURL());
					
					// converting tempVideo, and saving it to globalVideo.getVideoURL() path
					// ffmpeg -i inputVideo.flv -c:v libxvid -c:a aac -strict experimental outputVideo.mp4
					globalVideo = new Video(project.getProjectURL());
					System.out.println("converting " + tempVideo.getAbsolutePath() + " to " + globalVideo.getVideoURL());
					FFMPEGWrapper wrapper = new FFMPEGWrapper();
					
					String[] command;
					if(System.getProperty("os.name").toLowerCase().contains("mac")) {
						command = new String[] {
							wrapper.pathExecutable, 
							"-i", 
							tempVideo.getAbsolutePath(),
							"-c:v",
							"libx264",
							"-c:a",
							"aac",
							"-strict",
							"experimental",
							globalVideo.getVideoURL()
						};
					} else {
						command = new String[] {
							wrapper.pathExecutable, 
							"-i", 
							tempVideo.getAbsolutePath(),
							"-c:v",
							"libxvid",
							"-c:a",
							"aac",
							"-strict",
							"experimental",
							globalVideo.getVideoURL()
						};
					}
					GeneralUtils.runProcess(command);
					
					if(!System.getProperty("os.name").toLowerCase().contains("mac")) {
						command = new String[] {
								wrapper.pathExecutable, 
								"-i", 
								globalVideo.getVideoURL(),
								"-c:v",
								"libx264",
								"-c:a",
								"aac",
								"-strict",
								"experimental",
								globalVideo.getVideoURL()
							};
						GeneralUtils.runProcess(command);
					}
					
					Video screenVideo = new Video(globalVideo.getVideoURL(), project.getProjectURL());
//				
					// get the duration of the output video
					SegmentService.addVideo(project, s ,screenVideo);
					FFMPEGWrapper ffmpegwrapper = new FFMPEGWrapper();
					long duration = ffmpegwrapper.getDuration(s.getVideo().getVideoURL());
					s.setTime(duration);
				
					// Call.workspace.deleteList.add(f);
					Call.workspace.deleteList.add(new File(s.getSlide().getTempAudioURL()));
					Call.workspace.deleteList.add(tempVideo);
				
					s.getSlide().setTempAudioToNull();
					s.getSlide().setTempMuteVideoToNull();
				}
			}
		}
		Call.workspace.endOperation();
	}
}
