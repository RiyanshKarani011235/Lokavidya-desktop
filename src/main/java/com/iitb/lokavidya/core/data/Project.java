package com.iitb.lokavidya.core.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.utils.GeneralUtils;

/**
 * A Project.
 */

public class Project implements Serializable {

	public Project() {
		this.id = GeneralUtils.generateRandomNumber(11).intValue();
		orderingSequence = new ArrayList<Integer>();
		segments = new HashMap<Integer, Segment>();
	}

	private String projectURL;

	private String projectName;
	private Double projectSize;

	private Map<Integer, Segment> segments = new HashMap<Integer, Segment>();
	private Integer id;
	private List<Integer> orderingSequence = new ArrayList<Integer>();

	public Project(Project currentProject) {
		try {
			// TODO Auto-generated constructor stub
			setProjectURL(currentProject.getProjectURL());
			setProjectName(currentProject.getProjectName());
			setProjectSize(currentProject.getProjectSize());
			this.segments.putAll(currentProject.getSegmentsMap());
			setId(currentProject.getId());
			setOrderingSequence(currentProject.getOrderingSequence());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List<Segment> copyOrderingSequence() {
		List<Segment> l = new ArrayList<Segment>();
		for (Segment s : getOrderedSegmentList()) {
			l.add(new Segment(s));
			System.out.println("Copied one segment");
		}

		return l;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getProjectSize() {
		return projectSize;
	}

	public void setProjectSize(Double projectSize) {
		this.projectSize = projectSize;
	}

	// TODO
	public List<Segment> getSegment(Integer id) {
		List<Segment> segmentList = new ArrayList<Segment>();
		return segmentList;
	}

	// TODO
	public void setSegment(Segment segment) {

	}

	public Map<Integer, Segment> getSegmentsMap() {
		return segments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Project project = (Project) o;
		return Objects.equals(id, project.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Project{" + "id=" + id + ", projectSize='" + projectSize + "'" + '}';
	}

	public void addSegment(Segment segment) {

		segments.put(segment.getId(), segment);
		if (!orderingSequence.contains(segment.getId()))
			orderingSequence.add(segment.getId());
		else {
			System.out.println("Added");
		}

		for (Integer i : orderingSequence) {
			System.out.println("ORDER:" + i);
		}

		ProjectService.persist(this);
	}

	public void deleteSegment(Segment segment) {
		File file = null;
		if (segment.getVideo() == null) {
			if (segment.getSlide().getAudio() != null) {
				file = new File(segment.getSlide().getAudio().getAudioURL());
				if (file.exists()) {
					file.delete();
				}
			}
			file = new File(segment.getSlide().getImageURL());
			if (file.exists()) {
				file.delete();
			}
			file = new File(segment.getSlide().getPptURL());
			if (file.exists()) {
				file.delete();
			}
		} else {
			file = new File(segment.getVideo().getVideoURL());
			if (file.exists()) {
				file.delete();
			}
		}
		segments.remove(segment.getId());
		orderingSequence.remove(segment.getId());
		ProjectService.persist(this);

	}

	public void softDeleteSegment(Segment segment) {

		segments.remove(segment.getId());
		orderingSequence.remove(segment.getId());
		ProjectService.persist(this);

	}

	// Manipulate ordering sequence
	public void swapSegment(Segment segment1, Segment segment2) {
		int pos1 = orderingSequence.indexOf(segment1.getId());
		int pos2 = orderingSequence.indexOf(segment2.getId());
		Collections.swap(orderingSequence, pos1, pos2);
		ProjectService.persist(this);
	}

	public String getProjectURL() {
		return projectURL;
	}

	public void setProjectURL(String projectURL) {
		this.projectURL = projectURL;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Integer> getOrderingSequence() {
		return orderingSequence;
	}

	public void setOrderingSequence(List<Integer> orderingSequence) {
		this.orderingSequence = orderingSequence;
	}

	public void insertAt(int index1, int index2) {
		int tempNum = orderingSequence.get(index2);
		orderingSequence.remove(index2);
		orderingSequence.add(index1, tempNum);
		for (Integer i : orderingSequence) {
			// segmentList.add(segments.get(i));
			// System.out.println("Inside Ordering Sequence"+i);
		}
		ProjectService.persist(this);
	}

	public List<Segment> getOrderedSegmentList() {
		System.out.println("getOrderedSegmentList : called");
		List<Segment> segmentList = new ArrayList<Segment>();
		System.out.println("Size of the ordering segment list" + orderingSequence.size());
		for (Integer i : orderingSequence) {
			segmentList.add(segments.get(i));
			System.out.println("Inside Ordering Sequence" + i);
		}
		System.out.println("exited for loop, returning");
		System.out.println(segmentList);
		return segmentList;
	}

	public void setOrderedSegmentList(List<Segment> stateList) {

		try {
			for (Segment segment : getOrderedSegmentList())
				softDeleteSegment(segment);
			System.out.println(stateList.size());
			for (Segment s : stateList) {
				addSegment(new Segment(s));
				System.out.println("Added segment to current project from state");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Segment getSlideSegment(int slideNumber) {
		int index = 0;
		for (Segment s : getOrderedSegmentList()) {
			if (s.getSlide() != null) {
				if (index == slideNumber) {
					return s;
				}
				index++;
			}
		}
		return null;
	}

	public int indexOf(Segment x) {
		return getOrderedSegmentList().indexOf(x);
	}

	public Segment getVideo(int index) {
		int i = 0;
		for (Segment s : getOrderedSegmentList()) {
			if (!(s.getSlide() != null)) {
				if (i == index)
					return s;
				i++;
			}
		}
		return null;
	}

	public int getVideoNumber(Segment x) {
		int index = 0;
		for (Segment s : getOrderedSegmentList()) {
			if (s.getVideo() != null) {
				if (s.equals(x)) {
					return index;
				}
				index++;
			}
		}
		return index;
	}

	public void refresh() {
		for (Segment s : getOrderedSegmentList()) {
			if (s.getSlide() != null) {
				s.getSlide().refresh();
			}
		}
	}

	public String getProjectJsonPath() {
		return new File(projectURL, (projectName + ".json")).getAbsolutePath();

	}
}
