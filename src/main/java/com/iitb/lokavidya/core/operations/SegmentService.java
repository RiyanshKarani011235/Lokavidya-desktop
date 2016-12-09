package com.iitb.lokavidya.core.operations;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.iitb.lokavidya.core.data.Audio;
import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Slide;
import com.iitb.lokavidya.core.data.Video;
import com.iitb.lokavidya.core.utils.GeneralUtils;

public class SegmentService {
	
	public static void editAudio(Project project, Segment segment, Audio audio) {
		deleteAudio(project,segment);
		//Move audio to project location and then add to audio object.
		segment.getSlide().setAudio(audio);
		ProjectService.persist(project);
		
	}
	
	public static void deleteAudio(Project project,Segment segment) {
		if(segment.getSlide().getAudio()!=null)
		{
			File file = new File(segment.getSlide().getAudio().getAudioURL());
			System.out.println("The audio is :"+file.getAbsolutePath());
			if(file.exists()){
				System.out.println("Found and deleting");
				file.delete();
			}
			Slide slide = segment.getSlide();
			slide.setAudio(null);
			segment.setSlide(slide);
			ProjectService.persist(project);
		}
	}
	public static void softDeleteAudio(Project project,Segment segment) {
		if(segment.getSlide().getAudio()!=null)
		{
			
			Slide slide = segment.getSlide();
			slide.setAudio(null);
			segment.setSlide(slide);
			ProjectService.persist(project);
		}
	}
	public static void editImageSlide(Project project, Segment segment, String imageURL, String imageResolution, double imageFileSize) {
		deleteImage(project,segment);
		segment.getSlide().setImageURL(imageURL);
		segment.getSlide().setImageResolution(imageResolution);
		segment.getSlide().setImageFileSize(imageFileSize);
		ProjectService.persist(project);
	}
	public static void deleteImage(Project project,Segment segment) {
		File file = new File(segment.getSlide().getImageURL());
		if(file.exists()){
			file.delete();
		}
		segment.getSlide().setImageURL(null);
		ProjectService.persist(project);
	}
	public static void validatePresentation(Project project,Segment segment) {
		if(!segment.getSlide().getPptURL().endsWith("odp"))
		{
			
			ProjectService.persist(project);
			return;
		}
	}
	public static void addPresentation(Project project,Segment segment,String presentationURL) {
		String presentationName=RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		String newFilePath="";
		if (presentationURL.endsWith(".ppt")||(presentationURL.endsWith(".pptx"))) {
			newFilePath =new File(project.getProjectURL(),presentationName+".odp").getAbsolutePath();
			GeneralUtils.convertPptToOdp(presentationURL, newFilePath);
		}
	//	File newFile=new File(project.getProjectURL(),presentationName+".odp");
	//	File oldFile=new File(presentationURL);
	/*	try {
			FileUtils.copyFile(oldFile,newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		segment.getSlide().setPptURL(newFilePath);
		
		ProjectService.persist(project);
	}
	public static void convertPresentation(Segment s, Project p)
	{
		String oldPath=s.getSlide().getPptURL(),newPath;
		
		if (oldPath.endsWith(".ppt")||(oldPath.endsWith(".pptx"))) {
			System.out.println("Inside ConvetPresentation");
			String ext = FilenameUtils.getExtension(oldPath);
			newPath = oldPath.replace(ext, "odp");
			GeneralUtils.convertPptToOdp(oldPath, newPath);
			s.getSlide().setPptURL(newPath);
			ProjectService.persist(p);
		}
	}
	public static void addTempPresentation(Project project,Segment segment,String presentationURL) {
		String presentationName=RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		
		File newFile=new File(project.getProjectURL(),(presentationName+"."+FilenameUtils.getExtension(presentationURL)));
		File oldFile=new File(presentationURL);
		try {
			FileUtils.copyFile(oldFile,newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		segment.getSlide().setPptURL(newFile.getAbsolutePath());
		
		ProjectService.persist(project);
	}
	
	public static void editVideo(Project project, Segment segment, Video video){
		deleteVideo(project,segment);
		segment.setVideo(video);
		ProjectService.persist(project);
	}
	public static void addVideo(Project project, Segment segment, Video video){
		
		segment.setVideo(video);
		ProjectService.persist(project);
	}
	public static void deleteVideo(Project project,Segment segment) {
		if(segment.getVideo()!=null)
		{
			File file =  new File (segment.getVideo().getVideoURL());
			if(file.exists()) {
				file.delete();
			}
			segment.getVideo().setVideoDuration(0.0);
			segment.getVideo().setVideoFPS(0.0);
			segment.getVideo().setVideoResolution(null);
			segment.getVideo().setVideoSize(0.0);
			ProjectService.persist(project);
		}
	}	 
	public static void softDeleteVideo(Project project,Segment segment) {
		if(segment.getVideo()!=null)
		{
			segment.setVideo(null);
			/*segment.getVideo().setVideoDuration(0.0);
			segment.getVideo().setVideoFPS(0.0);
			segment.getVideo().setVideoResolution(null);
			segment.getVideo().setVideoSize(0.0);*/
			ProjectService.persist(project);
		}
	}	 
}
