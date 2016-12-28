package com.iitb.lokavidya.core.data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.PopupMenuListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.iitb.lokavidya.core.utils.GeneralUtils;

import java.util.Objects;

/**
 * A Slide.
 */

public class Slide implements Serializable {

	public boolean checkConstraints(String imageURL) {
		return true;
	}

	public void refresh() {
		GeneralUtils.convertPresentationToImage(this.presentationURL, imageURL);
	}

	/*
	 * Generate Slide object given an imageURL and audioURL
	 */
	// Create slide to import from Android
	public Slide(String sourceImageURL, String sourceAudioURL, String projectURL) {
		checkConstraints(sourceImageURL);

		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		audio = new Audio(sourceAudioURL, projectURL);
		String imageName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		try {
			FileUtils.copyFile(new File(sourceImageURL),
					new File(projectURL, imageName + "." + FilenameUtils.getExtension(sourceImageURL)));
			this.imageURL = new File(projectURL, imageName + "." + FilenameUtils.getExtension(sourceImageURL))
					.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		presentationURL = new File(projectURL, FilenameUtils.getBaseName(sourceImageURL) + ".pptx").getAbsolutePath();
		System.out.println("PresentationURL" + presentationURL);
		GeneralUtils.convertImageToPresentation(sourceImageURL, presentationURL);

		populateProperties(sourceImageURL);
	}

	private void populateProperties(String imageURL) {

	}

	/*
	 * Generate Slide object given only imageURL
	 */
	// Create slide while importing from Android project
	public Slide(String sourceImageURL, String projectURL) {
		checkConstraints(sourceImageURL);

		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		String imageName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
		try {
			FileUtils.copyFile(new File(sourceImageURL),
					new File(projectURL, imageName + "." + FilenameUtils.getExtension(sourceImageURL)));
			this.imageURL = new File(projectURL, imageName + "." + FilenameUtils.getExtension(sourceImageURL))
					.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		presentationURL = new File(projectURL, imageName + ".odp").getAbsolutePath();
		GeneralUtils.convertImageToPresentation(sourceImageURL, presentationURL);
		this.setImageURL(
				new File(projectURL, imageName + "." + FilenameUtils.getExtension(sourceImageURL)).getAbsolutePath());
		populateProperties(sourceImageURL);
	}

	/*
	 * 
	 * Generate Blank Slide object given only presentationURL
	 * 
	 */
	public Slide(String projectURL) {
		try {
			populateProperties(presentationURL);

			File ppt = new File(new File("resources").getAbsolutePath(), "BlankSlide.odp");
			String presentationURL = ppt.getAbsolutePath();
			String imgURL = new File(new File("resources").getAbsolutePath(), "BlankImage.png").getAbsolutePath();
			this.id = GeneralUtils.generateRandomNumber(11).intValue();
			System.out.println("pres URL: " + presentationURL);
			System.out.println("proj URL: " + projectURL);
			String presentationName = RandomStringUtils.randomAlphanumeric(10).toLowerCase() + ".odp";
			FileUtils.copyFile(new File(presentationURL), new File(projectURL, presentationName));

			if (new File(projectURL, presentationName).exists()) {
				this.presentationURL = new File(projectURL, presentationName).getAbsolutePath();
				this.imageURL = new File(projectURL, FilenameUtils.getBaseName(presentationName) + ".png")
						.getAbsolutePath();
				System.out.println("Created " + this.imageURL);
				FileUtils.copyFile(new File(imgURL), new File(this.imageURL));
			}

			populateProperties(presentationURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Generate Slide object given only presentationURL
	 */
	public Slide(String presentationURL, String projectURL, boolean presentation) {
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		// Import presentation
		if (presentation) {
			populateProperties(presentationURL);

			File ppt = new File(presentationURL);
			presentationURL = ppt.getAbsolutePath();

			String presentationName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();

			try {
				FileUtils.copyFile(new File(presentationURL), new File(projectURL, (presentationName + ".odp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (new File(projectURL, presentationName + ".odp").exists()) {
				this.presentationURL = new File(projectURL, presentationName + ".odp").getAbsolutePath();
				this.imageURL = new File(projectURL, FilenameUtils.getBaseName(presentationName) + ".png")
						.getAbsolutePath();
				GeneralUtils.convertPresentationToImage(this.presentationURL, imageURL);
			}
		}
		// Import only image from presentation
		// populateProperties(presentationURL);
		else {
			System.out.println("Only adding image");

			String sourceImageURL = presentationURL;
			checkConstraints(sourceImageURL);

			// this.id= GeneralUtils.generateRandomNumber(11).intValue();
			String imageName = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
			File newFile = new File(projectURL, imageName + "." + FilenameUtils.getExtension(sourceImageURL));
//			new File(sourceImageURL).renameTo(newFile);
			try {
				FileUtils.copyFile(new File(sourceImageURL), newFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.imageURL = newFile.getAbsolutePath();
			populateProperties(sourceImageURL);
		}
	}

	private String tempMuteVideo;
	private String tempAudio;
	private String imageURL;
	private String presentationURL;
	private String imageResolution;
	private Double imageFileSize;
	private Double presentationFileSize;
	private Audio audio;
	private String imageFormat;
	private String presentationFormat;
	private Integer id;

	public Slide (Slide s) {
		try {
			System.out.println("Setting slide");
			setId(s.getId());
			setImageURL(s.getImageURL());
			setPptURL(s.getPptURL());
			setImageResolution(s.getImageResolution());
			setImageFileSize(s.getImageFileSize());
			setPptFileSize(s.getPptFileSize());
			setAudio(new Audio(s.getAudio()));
			imageFormat = s.imageFormat;
			presentationFormat = s.presentationFormat;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getPptURL() {
		return presentationURL;
	}

	public void setPptURL(String pptURL) {
		this.presentationURL = pptURL;
	}

	public String getImageResolution() {
		return imageResolution;
	}

	public void setImageResolution(String imageResolution) {
		this.imageResolution = imageResolution;
	}

	public Double getImageFileSize() {
		return imageFileSize;
	}

	public void setImageFileSize(Double imageFileSize) {
		this.imageFileSize = imageFileSize;
	}

	public Double getPptFileSize() {
		return presentationFileSize;
	}

	public void setPptFileSize(Double pptFileSize) {
		this.presentationFileSize = pptFileSize;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Slide slide = (Slide) o;
		return Objects.equals(id, slide.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Slide{" + "id=" + id + ", imageURL='" + imageURL + "'" + ", pptURL='" + presentationURL + "'"
				+ ", imageResolution='" + imageResolution + "'" + ", imageFileSize='" + imageFileSize + "'"
				+ ", pptFileSize='" + presentationFileSize + "'" + '}';
	}

	public void setTempMuteVideoURL(String tempPath) {
		this.tempMuteVideo = tempPath;
	}

	public String getTempMuteVideoURL() {
		return this.tempMuteVideo;
	}

	public void setTempAudioURL(String tempPath) {
		this.tempAudio = tempPath;
	}

	public String getTempAudioURL() {
		return this.tempAudio;
	}

	public void setTempAudioToNull() {
		this.tempAudio = null;
	}

	public void setTempMuteVideoToNull() {
		this.tempMuteVideo = null;
	}
}
