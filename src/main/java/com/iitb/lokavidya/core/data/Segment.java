package com.iitb.lokavidya.core.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.iitb.lokavidya.core.utils.GeneralUtils;
import com.iitb.lokavidya.core.data.*;

import java.util.Objects;

/**
 * A Segment.
 */

public class Segment implements Serializable {

	public enum SegmentType {
		IMAGE, VIDEO, PRES
	}

	// For a blank segment
	public Segment(String projectURL) {
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		slide = new Slide(projectURL);
	}

	public Segment(String projectURL, boolean b) {
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
	}

	// For Import from Android Project
	public Segment(String sourceImageURL, String sourceAudioURL, String projectURL) {
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		slide = new Slide(sourceImageURL, sourceAudioURL, projectURL);
	}

	// For Import from Presentation/Video
	public Segment(String sourceURL, SegmentType e, String projectURL)//
	{
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		if (e.equals(SegmentType.IMAGE)) {
			slide = new Slide(sourceURL, projectURL);
		} else if (e.equals(SegmentType.VIDEO)) {
			video = new Video(sourceURL, projectURL);
		} else {
			slide = new Slide(sourceURL, projectURL);
		}
	}

	private Slide slide;

	private Video video;

	private long time = 0;

	private Set<LayeringObject> layeringObjects = new HashSet<LayeringObject>();

	private Set<Reference> references = new HashSet<Reference>();

	private Integer id;

	public Segment(Segment s) {
		try {
			System.out.println("Setting segment");
			setId(s.getId());
			if (s.getSlide() != null)
				setSlide(new Slide(s.getSlide()));
			if (s.getVideo() != null)
				setVideo(new Video(s.getVideo()));
			System.out.println("Returning from segment");
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

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return this.time;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Set<LayeringObject> getLayeringObjects() {
		return layeringObjects;
	}

	public void setLayeringObjects(Set<LayeringObject> layeringObjects) {
		this.layeringObjects = layeringObjects;
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}

	public void clearSegment() {
		slide = null;
		video = null;
		layeringObjects.clear();
		references.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Segment segment = (Segment) o;
		return Objects.equals(id, segment.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Segment{" + "id=" + id + '}';
	}

}
