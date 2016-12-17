package com.iitb.lokavidya.core.data;




import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.GeneralUtils;

/**
 * A Video.
 */

public class Video implements Serializable {

	public Video(String videoURL,String projectURL) {
		
		//check constraints TODO
		FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper();
		
		// TODO: video name should be Time based, not generated randomly
		this.id= GeneralUtils.generateRandomNumber(11).intValue();
		String videoName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		ffmpegWrapper.standardizeResolutionMaintainAspectRatio(videoURL, projectURL, videoName);
		this.videoURL = new File(projectURL,videoName+"."+FilenameUtils.getExtension(videoURL)).getAbsolutePath();
		ffmpegWrapper.processVideo(this.videoURL);
	}
	
	public Video (String projectURL) {
		this.id= GeneralUtils.generateRandomNumber(11).intValue();
		String videoName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		this.videoURL = new File(projectURL,videoName+".mp4").getAbsolutePath();
	}
	
   

	private String videoURL;

    private String videoResolution;

    private Double videoDuration;

    private Double videoFPS;

    private Double videoSize;

    private Integer id;
    
    private String videoFormat;
    
    public Video(Video v)
    {
    	System.out.println("Setting video");
    	
    	try {
			this.setVideoURL(v.getVideoURL());
			this.setVideoResolution(v.getVideoResolution());
			this.setVideoDuration(v.getVideoDuration());
			this.setVideoFPS(v.getVideoFPS());
			this.setVideoSize(v.getVideoSize());
			this.setId(v.getId());
			this.setVideoFormat(v.getVideoFormat());
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Returning from video");
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    public Double getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Double videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Double getVideoFPS() {
        return videoFPS;
    }

    public void setVideoFPS(Double videoFPS) {
        this.videoFPS = videoFPS;
    }

    public Double getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Double videoSize) {
        this.videoSize = videoSize;
    }
    
    public void setVideoProperties() {
//    	String osname=System.getProperty("os.name");
//	//	System.out.println(osname);
//		String pathExecutable = null;
//		JsonObject jobj = null;
//		if (osname.contains("Windows"))
//		{
//		//	System.out.println("Setting to Windows");
//			pathExecutable = new File(new File(new File(new File("resources").getAbsolutePath(),"ffmpeg"),"bin").getAbsolutePath(),"ffprobe.exe").getAbsolutePath();
//		}
//		else if(osname.toLowerCase().contains("mac")){
//			pathExecutable = new File(
//					new File(new File("resources").getAbsolutePath(), "bin")
//							.getAbsolutePath(),
//					"ffmpeg").getAbsolutePath();
//		}
//		else
//		{
//			pathExecutable = new File(
//					new File(new File("ffmpeg").getAbsolutePath(), "bin")
//					.getAbsolutePath(),
//			"ffmpeg").getAbsolutePath();
//		//	pathExecutable = "ffmpeg";
//		}
		String pathExecutable = new FFMPEGWrapper().pathExecutable;
    	String [] command = new String [] {
    			pathExecutable,
    			"-v",
    			"quiet",
    			"-print_format",
    			"json",
    			"-show_format",
    			"-show_streams",
    			videoURL
    		};

    		Runtime run;
    		Process pr;
    		String s = null, jsonString = "";
    		try {
    			run = Runtime.getRuntime();
    			pr = run.exec(command);
    			BufferedReader stdInput = new BufferedReader(new 
         		InputStreamReader(pr.getInputStream()));
    			while ((s = stdInput.readLine()) != null) {
    	  			jsonString +=s;
    			}
    			JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);
    			if(jobj != null) {
    	//			System.out.println(jobj.toString());
    				JsonArray streamArray = jobj.getAsJsonArray("streams");
    				JsonObject videoObject = streamArray.get(0).getAsJsonObject();
    				System.out.println(videoObject.toString());
    				videoResolution = videoObject.get("width")+"x"+videoObject.get("height"); 

    				System.out.println("video resolution: "+videoResolution);
    				try{
    				videoDuration = videoObject.get("duration").getAsDouble();}
    				catch (Exception e)
    				{
    					JsonObject format=jobj.getAsJsonObject("format");
    					System.out.println(format.toString());
    					videoDuration= format.get("duration").getAsDouble();
    				}
    				System.out.println("duration: "+videoDuration);

    				String temp = videoObject.get("avg_frame_rate").getAsString();
    				int frame = Integer.parseInt(temp.split("/")[0]);
    				int sec = Integer.parseInt(temp.split("/")[1]);
    				videoFPS = (double)frame/sec;
    		//		System.out.println("video FPS: "+videoFPS);
    				JsonObject videoFormatObject = jobj.getAsJsonObject("format");
    				videoSize = videoFormatObject.get("size").getAsDouble();
    	//			System.out.println("video size: "+videoSize);
    				videoFormat = videoFormatObject.get("format_name").getAsString();
    	//			System.out.println("Video format: "+videoFormat);
    			}
    			else{
    				System.out.println("null");
    			}
    		} 
    		catch  (Throwable e) {
    			e.printStackTrace();
    		}

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Video{" +
            "id=" + id +
            ", videoURL='" + videoURL + "'" +
            ", videoResolution='" + videoResolution + "'" +
            ", videoDuration='" + videoDuration + "'" +
            ", videoFPS='" + videoFPS + "'" +
            ", videoSize='" + videoSize + "'" +
            '}';
    }

	public String getVideoFormat() {
		return videoFormat;
	}

	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
}
