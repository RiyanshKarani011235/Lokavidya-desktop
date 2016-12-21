package com.iitb.lokavidya.core.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
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
 * A Audio.
 */

public class Audio implements Serializable {

	public Audio(String audioURL, String projectURL) {

		// check constraints TODO

		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		String audioName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		try {
			FileUtils.copyFile(new File(audioURL),
					new File(projectURL, audioName + "." + FilenameUtils.getExtension(audioURL)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// populate the attributes TODO

		this.setAudioURL(
				new File(projectURL, audioName + "." + FilenameUtils.getExtension(audioURL)).getAbsolutePath());
		setAudioProperties();
	}

	public Audio(String projectURL) {
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		String audioName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		this.setAudioURL(new File(projectURL, audioName + ".wav").getAbsolutePath());
	}

	public Audio() {

	}

	private Double audioDuration;

	private Double audioBitrate;

	private Double audioFileSize;

	private String audioURL;

	private Integer id;

	private String audioFormat;

	public Audio(Audio a) {
		setAudioDuration(a.getAudioDuration());
		setAudioBitrate(a.getAudioBitrate());
		setAudioFileSize(a.getAudioFileSize());
		setAudioURL(a.getAudioURL());
		setId(a.getId());
		setAudioFormat(a.getAudioFormat());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAudioDuration() {
		return audioDuration;
	}

	public void setAudioDuration(Double audioDuration) {
		this.audioDuration = audioDuration;
	}

	public Double getAudioBitrate() {
		return audioBitrate;
	}

	public void setAudioBitrate(Double audioBitrate) {
		this.audioBitrate = audioBitrate;
	}

	public Double getAudioFileSize() {
		return audioFileSize;
	}

	public void setAudioFileSize(Double audioFileSize) {
		this.audioFileSize = audioFileSize;
	}

	public String getAudioURL() {
		return this.audioURL;
	}

	public void setAudioURL(String audioURL) {
		this.audioURL = audioURL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Audio audio = (Audio) o;
		return Objects.equals(id, audio.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Audio{" + "id=" + id + ", audioDuration='" + audioDuration + "'" + ", audioBitrate='" + audioBitrate
				+ "'" + ", audioFileSize='" + audioFileSize + "'" + '}';
	}

	public void setAudioProperties() {
		// String osname=System.getProperty("os.name");
		//// System.out.println(osname);
		// String pathExecutable = null;
		// JsonObject jobj = null;
		// if (osname.contains("Windows"))
		// {
		// // System.out.println("Setting to Windows");
		// pathExecutable = new File(new File(new File(new
		// File("resources").getAbsolutePath(),"ffmpeg"),"bin").getAbsolutePath(),"ffprobe.exe").getAbsolutePath();
		// }
		// else if(osname.toLowerCase().contains("mac")){
		// pathExecutable = new File(
		// new File(new File("resources").getAbsolutePath(), "bin")
		// .getAbsolutePath(),
		// "ffmpeg").getAbsolutePath();
		// }
		// else
		// {
		//
		// pathExecutable = new File(
		// new File(new File("ffmpeg").getAbsolutePath(), "bin")
		// .getAbsolutePath(),
		// "ffmpeg").getAbsolutePath();
		//
		// // pathExecutable = "ffmpeg";
		// }
		String pathExecutable = new FFMPEGWrapper().pathExecutableffprobe;
		JsonObject jobj = null;
		System.out.println("Audio.java : ironstein : run command");
		String[] command = new String[] { pathExecutable, "-v", "quiet", "-print_format", "json", "-show_format",
				"-show_streams", audioURL };

		System.out.println("abbaaaamxcffskldjhjjlkasdjflkjkl");
		for(int i=0; i<command.length; i++) {
			System.out.print(command[i] + " ");
		}System.out.println();
		
		Runtime run;
		Process pr;
		String s = null, jsonString = "";
		try {
			run = Runtime.getRuntime();
			pr = run.exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((s = stdInput.readLine()) != null) {
				jsonString += s;
			}

			jobj = new Gson().fromJson(jsonString, JsonObject.class);
			if (jobj != null) {
				// System.out.println(jobj.toString());
				JsonArray streamArray = jobj.getAsJsonArray("streams");
				JsonObject audioObject = streamArray.get(0).getAsJsonObject();
				audioDuration = audioObject.get("duration").getAsDouble();
				// System.out.println("Audio duration: "+audioDuration);
				audioBitrate = audioObject.get("bit_rate").getAsDouble();
				// System.out.println("Audio bir rate: "+audioBitrate);
				JsonObject audioFormatObject = jobj.getAsJsonObject("format");
				audioFileSize = audioFormatObject.get("size").getAsDouble();
				// System.out.println("File size: "+audioFileSize);
				audioFormat = audioFormatObject.get("format_name").getAsString();
				// System.out.println("audio format: "+audioFormat);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}
}
