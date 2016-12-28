package com.iitb.lokavidya.core.operations;

import gui.Call;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import poi.PptToImages;
import poi.PptxToImages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iitb.lokavidya.core.data.Audio;
import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Slide;
import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.GeneralUtils;

import Dialogs.OpenPdf;
import Dialogs.OpenPresentation;

public class ProjectService {

	private static List<String> pptPaths = new ArrayList();
	private static boolean checkHash = true;

	public static Project createNewProject(String projectURL) {
		if (new File(projectURL).isDirectory())
			try {
				System.out.println("Deleting");
				FileUtils.cleanDirectory(new File(projectURL + File.separator));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else {
			System.out.println("Returning null");
			return null;
		}

		Project project = new Project();
		String projectName = new File(projectURL).getName();
		System.out.println(projectName);
		project.setProjectName(projectName);
		System.out.println(projectURL);
		project.setProjectURL(projectURL);

		// List<Segment> segmentList = new ArrayList<Segment>();
		List<Integer> orderingSequence = new ArrayList<Integer>();
		project.setOrderingSequence(orderingSequence);

		Segment segment = new Segment(projectURL);
		project.addSegment(segment);
		
		// make the currentSegment as the first segment of the project
		// Ironstein 28-12-16
		Call.workspace.currentSegment = project.getSegmentsMap().get(project.getOrderingSequence().get(0));

		ProjectService.persist(project);
		GeneralUtils.stopOfficeInstance();
		return project;

	}

	public static Project createNewProject(String projectURL, boolean checkForHash) {
		if (checkForHash == false) {
			checkHash = false;
		}
		return createNewProject(projectURL);
	}

	public static boolean isFileExist(String fileURL) {
		if (fileURL != null && (!new File(fileURL).exists() || new File(fileURL).isDirectory())) {
			return false;
		}
		return true;
	}

	public static Project getInstance(String pathToProjectJson) {
		System.out.println("Retrieving instance from JSON: Start");
		Project project = null;

		System.out.println("checking sanctity of JSON file");

		// Try generating GSON object from JSON file
		Gson gson = new Gson();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToProjectJson));
			project = gson.fromJson(bufferedReader, new TypeToken<Project>() {
			}.getType());
		} catch (Exception e) {
			// if there is an error, return null and the project import will be
			// unsuccessfull
			e.printStackTrace();
			return null;
		}
		System.out.println("Retrieving instance from JSON:END");

		// check if JSON has been modified
		try {
			FileInputStream fis = new FileInputStream(pathToProjectJson);
			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			fis.close();
			System.out.println("md5File path : "
					+ FilenameUtils.concat(project.getProjectURL(), project.getProjectName() + ".hash"));

			File md5File = new File(FilenameUtils.concat(project.getProjectURL(), project.getProjectName() + ".hash"));
			String md5ReadString = FileUtils.readFileToString(md5File, "UTF-8");
			if (!md5.equals(md5ReadString)) {
				// if the JSON is corrupt, return null and the project import
				// will be unsuccessfull
				System.out.println("Corrupt JSON");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// check if all files as defined in the JSON file exist in the project
		// if any file is missing, return null and the project import will be
		// unsuccessfull
		for (Segment segment : project.getOrderedSegmentList()) {
			if (segment.getSlide() != null) {
				// has slide
				Audio audio = segment.getSlide().getAudio();
				String audioURL = null;
				String imageURL = segment.getSlide().getImageURL();
				String presentationURL = segment.getSlide().getPptURL();

				if (audio != null) {
					audioURL = audio.getAudioURL();
					// checking audio file
					if (!isFileExist(audioURL)) {
						System.out.println("audio file not found : " + audioURL);
						return null;
					}
				}

				// checking image file
				if (!isFileExist(imageURL)) {
					System.out.println("image file not found : " + imageURL);
					return null;
				}

				// checking presentation file
				if (!isFileExist(presentationURL)) {
					System.out.println("presentation not found : " + presentationURL);
					return null;
				}

			} else if (segment.getVideo() != null) {
				// has video
				String videoURL = segment.getVideo().getVideoURL();
				// checking for video file
				if (!isFileExist(videoURL)) {
					return null;
				}
			} else {
				// blank slide
			}
		}

		File jsonFile = new File(pathToProjectJson);
		File projectFolder = jsonFile.getParentFile();
		String oldprojectPath = project.getProjectURL();
		if (!projectFolder.getAbsolutePath().equals(oldprojectPath)) {
			System.out.println("Changing");
			project.setProjectURL(projectFolder.getAbsolutePath());
			List<Segment> segmentList = project.getOrderedSegmentList();
			for (int i = 0; i < segmentList.size(); i++) {
				if (segmentList.get(i).getSlide() != null) {
					segmentList.get(i).getSlide().setImageURL(segmentList.get(i).getSlide().getImageURL()
							.replace(oldprojectPath, projectFolder.getAbsolutePath()));
					segmentList.get(i).getSlide().setPptURL(segmentList.get(i).getSlide().getPptURL()
							.replace(oldprojectPath, projectFolder.getAbsolutePath()));
					if (segmentList.get(i).getSlide().getAudio() != null) {
						segmentList.get(i).getSlide().getAudio().setAudioURL(segmentList.get(i).getSlide().getAudio()
								.getAudioURL().replace(oldprojectPath, projectFolder.getAbsolutePath()));
						FFMPEGWrapper ffmpegwrapper = new FFMPEGWrapper();
						long duration = ffmpegwrapper
								.getDuration(segmentList.get(i).getSlide().getAudio().getAudioURL());
						segmentList.get(i).setTime(duration);
					}
				}
				if (segmentList.get(i).getVideo() != null) {
					segmentList.get(i).getVideo().setVideoURL(segmentList.get(i).getVideo().getVideoURL()
							.replace(oldprojectPath, projectFolder.getAbsolutePath()));
					FFMPEGWrapper ffmpegwrapper = new FFMPEGWrapper();
					long duration = ffmpegwrapper.getDuration(segmentList.get(i).getVideo().getVideoURL());
					segmentList.get(i).setTime(duration);
				}
			}
		}

		ProjectService.persist(project);
		System.out.println("Returning");
		return project;
	}

	public static void persist(Project project) {
		File jsonFile = new File(FilenameUtils.concat(project.getProjectURL(), project.getProjectName() + ".json"));
		if (!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Writer writer = new FileWriter(jsonFile.getAbsolutePath());
			Gson gson = new GsonBuilder().create();
			gson.toJson(project, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileInputStream fis = new FileInputStream(
					FilenameUtils.concat(project.getProjectURL(), project.getProjectName() + ".json"));
			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			fis.close();
			Writer writer = new FileWriter(
					FilenameUtils.concat(project.getProjectURL(), project.getProjectName() + ".hash"));
			writer.write(md5);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exportAndroidProject(String projectPath, String androidProjectPath) {
		Project project = ProjectService
				.getInstance(projectPath + File.separator + FilenameUtils.getName(projectPath) + ".json");
		String tmpPath = System.getProperty("java.io.tmpdir");
		File projectTmp = new File(tmpPath, project.getProjectName());
		FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper();
		if (projectTmp.exists())
			try {
				FileUtils.deleteDirectory(projectTmp);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		projectTmp.mkdir();
		File projectTmpImage = new File(projectTmp.getAbsolutePath(), "images");
		if (!projectTmpImage.exists())
			projectTmpImage.mkdir();
		File projectTmpAudio = new File(projectTmp.getAbsolutePath(), "audio");
		if (!projectTmpAudio.exists())
			projectTmpAudio.mkdir();
		List<Segment> segmentList = project.getOrderedSegmentList();
		int index = 1;
		String wavPath = new File(projectTmpAudio, "temp.wav").getAbsolutePath();
		for (Segment s : segmentList) {
			System.out.println("HIT!");
			if (s.getSlide() != null) {
				try {
					FileUtils.copyFile(new File(s.getSlide().getImageURL()),
							new File(projectTmpImage, project.getProjectName() + "." + index + "."
									+ FilenameUtils.getExtension(s.getSlide().getImageURL())));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (s.getSlide().getAudio() != null) {
						File file = new File(wavPath);
						if (file.exists())
							file.delete();
						ffmpegWrapper.convertMp3ToWav(s.getSlide().getAudio().getAudioURL(), wavPath);
						FileUtils.copyFile(new File(wavPath), new File(projectTmpAudio, project.getProjectName() + "."
								+ index + "." + FilenameUtils.getExtension(s.getSlide().getAudio().getAudioURL())));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				index++;
			}

		}
		File file = new File(wavPath);
		if (file.exists())
			file.delete();

		String imagesFolderPath = new File(new File(androidProjectPath,
				Paths.get(new File(projectPath).getAbsolutePath()).getFileName().toString()), "images")
						.getAbsolutePath();
		System.out.println(projectTmpImage.toString());
		File[] paths = projectTmpImage.listFiles();
		System.out.println("ironstein here");

		for (int i = 0; i < paths.length; i++) {
			// read a jpeg from a inputFile
			BufferedImage bufferedImage;
			try {
				if (FilenameUtils.getExtension(paths[i].getAbsolutePath()).equals("jpg")) {
					bufferedImage = ImageIO.read(paths[i]);
					String outputFile = new File(projectTmpImage, FilenameUtils.removeExtension(paths[i].getName()))
							.getAbsolutePath() + ".png";
					System.out.println(outputFile);
					ImageIO.write(bufferedImage, "png", new File(outputFile));
					paths[i].delete();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		GeneralUtils.createZip(projectTmp.getAbsolutePath(), androidProjectPath);
	}

	public static boolean importAndroidProject(String projectjsonpath, String zipPath) {

		Project project = getInstance(projectjsonpath);
		String tmpPath = System.getProperty("java.io.tmpdir");
		
		// deleting all folders with the same name as that of the zip file in tmpPath
		String projTmpDir = FilenameUtils.getBaseName(zipPath);
		File projectTmp = new File(tmpPath, projTmpDir);
		System.out.println("projectTmp : " + projectTmp);
		if(projectTmp.exists()) {
			try {
				FileUtils.deleteDirectory(projectTmp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("extracting zip");
		GeneralUtils.extractZip(zipPath, tmpPath);
		System.out.println("extracting zip complete");
		File projectTmpImage = new File(projectTmp.getAbsolutePath(), "images");
		File projectTmpAudio = new File(projectTmp.getAbsolutePath(), "audio");

		// check if the zip file consists of a valid project

		// check if the audio and image folders exist
		if ((!projectTmpImage.exists()) || (!projectTmpAudio.exists())) {
			return false;
		}
		
		System.out.println("projectTmpAudio.length() : " + projectTmpAudio.length());

		// checking the number of files in both the folders is the same, and not zero
		if ((!(projectTmpAudio.listFiles().length == projectTmpAudio.listFiles().length))
				|| (projectTmpAudio.listFiles().length == 0)) {
			return false;
		}

		List<File> audioFilesList = Arrays.asList(projectTmpAudio.listFiles());

		// comparator for comparing two files
		Comparator<File> fc = new Comparator<File>() {
			public int compare(File o1, File o2) {
				int i1, i2;

				i1 = Integer.parseInt(o1.getName().split("\\.")[1]);

				i2 = Integer.parseInt(o2.getName().split("\\.")[1]);

				System.out.println("" + i1 + i2);
				if (i1 > i2) {
					return 1;
				}
				if (i1 == i2) {
					return 0;
				} else
					return -1;
			}
		};
		
		// sort the filenames in increasing order
		Collections.sort(audioFilesList, fc);

		// check the names of the files should be same in both the folders
		for (File f : audioFilesList) {
			String name = FilenameUtils.getBaseName(f.getAbsolutePath()).split("\\.")[0];
			String index = FilenameUtils.getBaseName(f.getAbsolutePath()).split("\\.")[1];

			File imageFilePng = new File(projectTmpImage, name + "." + index + ".png");
			File imageFileJpg = new File(projectTmpImage, name + "." + index + ".jpg");
			if (imageFilePng.exists()) {
				Segment segment = new Segment(imageFilePng.getAbsolutePath(), f.getAbsolutePath(),
						project.getProjectURL());
				System.out.println("URL of the presentation:" + segment.getSlide().getPptURL());
				project.addSegment(segment);

			} else if (imageFileJpg.exists()) {
				Segment segment = new Segment(imageFileJpg.getAbsolutePath(), f.getAbsolutePath(),
						project.getProjectURL());
				System.out.println("URL of the presentation:" + segment.getSlide().getPptURL());
				project.addSegment(segment);
			} else {
				return false;
			}
		}

		GeneralUtils.stopOfficeInstance();
		ProjectService.persist(project);
		return true;
	}

	public static int importPresentationGenerateImages(String presentationURL, Project project, OpenPresentation window) {
		// returns -1 if anything went wrong, else returns the size of the presentation
		
		File file = new File(presentationURL);
		try {
			int size = 0;
			if (presentationURL.endsWith(".pptx")) {
				
				// generate images
				if(!new PptxToImages(presentationURL, project.getProjectURL()).run()) {
					return -1;
				}
				
				XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
				List<XSLFSlide> slides = ppt.getSlides();
				size = slides.size();
				window.setprogressvalue(50);
			} else if (presentationURL.endsWith(".ppt")) {
				FileInputStream out = new FileInputStream(file);
				
				// generate images
				if(!new PptToImages(presentationURL, project.getProjectURL()).run()) {
					out.close();
					return -1;
				}
				
				HSLFSlideShow ppt = new HSLFSlideShow(out);
				List<HSLFSlide> slides = ppt.getSlides();
				size = slides.size();
				window.setprogressvalue(50);
			}
			// System.out.println("It created ppt");
			return size;
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} return -1;
	}
	
	public static void importPresentationAddPresentationsToProject(String presentationURL, Project project, OpenPresentation window, int size) {
		
		BufferedWriter bw = null;
		// String tempPath = System.getProperty("java.io.tmpdir");
		try {
			bw = new BufferedWriter(new FileWriter("timelog.txt", true));
			long current_time = System.currentTimeMillis(), new_time, first_time = System.currentTimeMillis();
			String tempPath = project.getProjectURL();
			File file = new File(presentationURL);
			Future<String> res = createPresentations(presentationURL, tempPath, file, window);
			
			// int size=slides.size();
			List<Segment> newSegments = new ArrayList<Segment>();
			double divider = (double) 20 / (double) size;
			int value = 50;
			for (int i = 0; i < size; i++) {
				if (Call.workspace.cancelled) {
					System.out.println("Alert caught before res.get");
					return;
				} else {
					String tempImgPath = new File(project.getProjectURL(),
							("img_" + (Integer.toString(i + 1)) + ".jpg")).getAbsolutePath();
					Segment segment = new Segment(project.getProjectURL(), false);
					System.out.println("Creating" + tempImgPath);
					Slide slide = new Slide(tempImgPath, project.getProjectURL(), false);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for image creation is: " + (new_time - current_time));
					current_time = new_time;
					segment.setSlide(slide);
					newSegments.add(segment);
					project.addSegment(segment);
					ProjectService.persist(project);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for persistance is: " + (new_time - current_time));
					current_time = new_time;
				}
				value = (int) (50 + (double) (i + 1) * (divider));
				window.setprogressvalue(value);
			}

			res.get();
			divider = (double) 5 / (double) newSegments.size();
			GeneralUtils.stopOfficeInstance();
			String presentationName = FilenameUtils.getBaseName(presentationURL);
			int i = 0;
			for (Segment s : newSegments) {
				String pptPath = "";
				pptPath = new File(tempPath,
						presentationName + "." + i + "." + FilenameUtils.getExtension(presentationURL))
								.getAbsolutePath();

				System.out.println(pptPath);
				SegmentService.addPresentation(project, s, pptPath);
				value = (int) (70 + (double) (i + 1) * (divider));
				window.setprogressvalue(value);
				i++;
			}

			new_time = System.currentTimeMillis();
			bw.write("\nTime taken for updating segments: " + (new_time - current_time));
			current_time = new_time;
			bw.write("Total time: " + (current_time - first_time));
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e2) {
			e2.printStackTrace();
		} catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		
		
	}

	public static ArrayList<String> importPdfGenerateImages(String pdfUrl, Project project, OpenPdf window) {
		
		try {
			// create tmpImages directory
			File tmpImagesDirectory = new File("resources", "tmpImages");
			tmpImagesDirectory.mkdir();

			ArrayList<String> outputFilenamesList = new ArrayList<String>();

			// check if directory is created
			if (!(tmpImagesDirectory.exists() && tmpImagesDirectory.isDirectory())) {
				// could not create tmpImages directory
				JOptionPane.showMessageDialog(null,
						"Could not access the directory : " + tmpImagesDirectory.getAbsolutePath() + "\n"
								+ "make sure that you have read and write access to this directory",
						"", JOptionPane.INFORMATION_MESSAGE);
				return null;
			}
			
			// convert PDF to individual images
			PDDocument document;
			document = PDDocument.loadNonSeq(new File(pdfUrl), null);
			List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
			int page = 0;
			for (PDPage pdPage : pdPages) {
				++page;
				BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
				String outputFileName = new File(tmpImagesDirectory.getAbsolutePath(),
						new File(pdfUrl).getName() + "-" + page + ".jpg").getAbsolutePath();
				ImageIOUtil.writeImage(bim, outputFileName, 300);
				outputFilenamesList.add(outputFileName);
				
				// check if import is cancelled
				if(Call.workspace.cancelled) {
					document.close();
					System.out.println("cancelled");
					return null;
				}
			}
			document.close();
			return outputFilenamesList;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> importPdfGenerateImagesUsingGhostscript(String pdfUrl) {
		// create tmpImages directory
		File tmpImagesDirectory = new File(System.getProperty("java.io.tmpdir"), "tmpImages1");
		File tmpImagesDirectory2 = new File(System.getProperty("java.io.tmpdir"), "tmpImages2");

		System.out.println("tmpImagesDirectory : " + tmpImagesDirectory.getAbsolutePath());
		System.out.println("tmpImagesDirectory2 : " + tmpImagesDirectory2.getAbsolutePath());
		
		// creating tmpImagesDirectory
		tmpImagesDirectory.mkdir();
		tmpImagesDirectory2.mkdir();
		
		// cleaning tmpImagesDirectory
		try {
			FileUtils.cleanDirectory(tmpImagesDirectory);
			FileUtils.cleanDirectory(tmpImagesDirectory2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> outputImagesList = new ArrayList<String>();

		// check if directory is created
		if (!(tmpImagesDirectory.exists() && tmpImagesDirectory.isDirectory())) {
			// could not create tmpImages directory
			JOptionPane.showMessageDialog(null,
					"Could not access the directory : " + tmpImagesDirectory.getAbsolutePath() + "\n"
							+ "make sure that you have read and write access to this directory",
					"", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		
		// cleaning tmpImagesDirectory
		try {
			FileUtils.cleanDirectory(tmpImagesDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// convert PDF to individual images
		String ghostScriptPath = GeneralUtils.findGhostScriptPath();
		if(ghostScriptPath == null) {
			// ghostScript path not found
			return null;
		}
		
		String outputFileName = new File(tmpImagesDirectory.getAbsolutePath(),
				FilenameUtils.getBaseName(new File(pdfUrl).getName()) + "-%d.png").getAbsolutePath();
		String[] command = {
				ghostScriptPath,
				"-sDEVICE=png16m", 
				"-dTextAlphaBits=4", 
				"-r300", 
				"-o",
				outputFileName,
				pdfUrl
		};
		
		boolean b = GeneralUtils.runProcess(command);
		if((!b) || Call.workspace.cancelled) {
			return null;
		}
		
		// scale the image to fit the window width and window height
		String[] list = tmpImagesDirectory.list();
		for(int i=0; i<list.length; i++) {
			System.out.println("tmpImage : " + list[i]);
			BufferedImage rawShot;
			try {
				String inputImageName = new File(tmpImagesDirectory, list[i]).getAbsolutePath(); 
				rawShot = ImageIO.read(new File(inputImageName));
				int hVideo = Call.workspace.videoHeight;
				int wVideo = Call.workspace.videoWidth;
				int hShot = rawShot.getHeight();
				int wShot = rawShot.getWidth();

				int newImageHeight;
				int newImageWidth;
				
				if((hShot > hVideo) || (wShot > wVideo)) {
					// scale the image down
					if((hShot > hVideo) && (wShot <= wVideo)) {
						// scale down the height
						newImageHeight = hVideo;
						newImageWidth = (int) Math.ceil(hVideo * wShot / hShot);
					} else if((hShot <= hVideo) && (wShot > wVideo)) {
						// scale down the width
						newImageWidth = wVideo;
						newImageHeight = (int) Math.ceil(wVideo * hShot / wShot);
					} else {
						// scale down both
						if((int) Math.ceil(hVideo * wShot / hShot) > wVideo) {
							// the shot width should be the same as window width
							newImageWidth = wVideo;
							newImageHeight = (int) Math.ceil(wVideo * hShot / wShot);
						} else {
							// the shot height should be the same as window height
							newImageHeight = hVideo;
							newImageWidth = (int) Math.ceil(hVideo * wShot / hShot);
						}
					}
				} else {
					// scale the image up
					if(((int) Math.ceil(hVideo * wShot / hShot)) > wVideo) {
						// scale height to the maximum
						newImageHeight = hVideo;
						newImageWidth = (int) Math.ceil(hVideo * wShot / hShot);
					} else {
						// scale width to maximum
						newImageWidth = wVideo;
						newImageHeight = (int) Math.ceil(wVideo * hShot / wShot);
					}
				}
				
				String resizedImage = new File(tmpImagesDirectory2, new File(list[i]).getName()).getAbsolutePath();
				FFMPEGWrapper wrapper = new FFMPEGWrapper();
				String[] command_ = {
						wrapper.pathExecutable,
						"-i",
						inputImageName,
						"-vf",
						"scale=" + newImageWidth + ":" + newImageHeight,
						resizedImage
				};
				
				b = GeneralUtils.runProcess(command_);
				if((!b) || Call.workspace.cancelled) {
					return null;
				}
				outputImagesList.add(resizedImage);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("outpuImagesList : ");
		for(int i=0; i<outputImagesList.size(); i++) {
			System.out.println(outputImagesList.get(i));
		}
		return outputImagesList;
	}
	
	public static void importPdfAddImagesToProject(Project project, OpenPdf window, ArrayList<String> outputFilenamesList) {
		
		BufferedWriter bw = null;
		// String tempPath = System.getProperty("java.io.tmpdir");
		try {
			bw = new BufferedWriter(new FileWriter("timelog.txt", true));
			long current_time = System.currentTimeMillis(), new_time, first_time = System.currentTimeMillis();
			
			ArrayList<Segment> newSegments = new ArrayList<Segment>();
			for (String outputFilename : outputFilenamesList) {
				Segment segment = new Segment(project.getProjectURL(), false);
				Slide slide = new Slide(outputFilename, project.getProjectURL(), false);
				new_time = System.currentTimeMillis();
				bw.write("\nTime taken for image creation is: " + (new_time - current_time));
				current_time = new_time;
				segment.setSlide(slide);
				newSegments.add(segment);
				project.addSegment(segment);
				ProjectService.persist(project);
				new_time = System.currentTimeMillis();
				bw.write("\nTime taken for persistance is: " + (new_time - current_time));
				current_time = new_time;
			}
			
			new_time = System.currentTimeMillis();
			bw.write("\nTime taken for updating segments: " + (new_time - current_time));
			current_time = new_time;
			bw.write("Total time: " + (current_time - first_time));
			bw.close();
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	}

	public static Future<String> createPresentations(String presentationURL, String tempPath, File file,
			OpenPresentation window) throws IOException, FileNotFoundException {
		BufferedWriter bw = null;
		// String tempPath = System.getProperty("java.io.tmpdir");
		try {
			bw = new BufferedWriter(new FileWriter("timelog.txt", true));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long new_time, current_time;
		FileInputStream out = new FileInputStream(file);
		if (presentationURL.endsWith(".pptx")) {
			XMLSlideShow ppt = new XMLSlideShow(out);

			List<XSLFSlide> slides = ppt.getSlides();
			String presentationName = FilenameUtils.getBaseName(presentationURL);
			current_time = System.currentTimeMillis();
			double divider = (double) 30 / (double) slides.size();
			int value = 10;
			for (int i = 0; i < slides.size(); i++) {
				if (!Call.workspace.cancelled) {
					File tempPPTPath = new File(tempPath,
							presentationName + "." + i + "." + FilenameUtils.getExtension(presentationURL));
					FileUtils.copyFile(new File(presentationURL), tempPPTPath);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for copying the ppt: " + (new_time - current_time));
					current_time = new_time;

					keepSlide(tempPPTPath.getAbsolutePath(), i);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for poi to seperate the prsentation is: " + (new_time - current_time));
					current_time = new_time;
				} else {
					System.out.println("Alert caught");
					break;
				}
				value = (int) (10 + (double) (i + 1) * divider);
				window.setprogressvalue(value);
			}

		} else if (presentationURL.endsWith(".ppt")) {

			HSLFSlideShow ppt = new HSLFSlideShow(out);
			System.out.println("Creating slides");
			List<HSLFSlide> slides = ppt.getSlides();

			String presentationName = FilenameUtils.getBaseName(presentationURL);
			current_time = System.currentTimeMillis();
			double divider = (double) 30 / (double) slides.size();
			int value = 10;
			for (int i = 0; i < slides.size(); i++) {
				if (!Call.workspace.cancelled) {
					File tempPPTPath = new File(tempPath,
							presentationName + "." + i + "." + FilenameUtils.getExtension(presentationURL));
					FileUtils.copyFile(new File(presentationURL), tempPPTPath);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for copying the ppt: " + (new_time - current_time));
					System.out.println("\nTime taken for copying the ppt: " + (new_time - current_time));
					current_time = new_time;

					keepSlide(tempPPTPath.getAbsolutePath(), i + 1);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for poi to seperate the prsentation is: " + (new_time - current_time));
					System.out.println(
							"\nTime taken for poi to seperate the prsentation is: " + (new_time - current_time));
					current_time = new_time;
				} else {
					System.out.println("Alert caught");
					break;
				}
				value = (int) (10 + (double) (i + 1) * divider);
				window.setprogressvalue(value);
			}
		}
		// ppt.close();
		bw.close();
		Future<String> v = new Future<String>() {

			public boolean isDone() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}

			public String get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				// TODO Auto-generated method stub
				return null;
			}

			public String get() throws InterruptedException, ExecutionException {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean cancel(boolean mayInterruptIfRunning) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		return v;
	}

	public static Future<String> createPresentations(String presentationURL, String tempPath, File file)
			throws IOException, FileNotFoundException {
		BufferedWriter bw = null;
		// String tempPath = System.getProperty("java.io.tmpdir");
		try {
			bw = new BufferedWriter(new FileWriter("timelog.txt", true));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long new_time, current_time;
		FileInputStream out = new FileInputStream(file);
		if (presentationURL.endsWith(".pptx")) {
			XMLSlideShow ppt = new XMLSlideShow(out);

			List<XSLFSlide> slides = ppt.getSlides();
			String presentationName = FilenameUtils.getBaseName(presentationURL);
			current_time = System.currentTimeMillis();

			for (int i = 0; i < slides.size(); i++) {
				if (!Call.workspace.cancelled) {
					File tempPPTPath = new File(tempPath,
							presentationName + "." + i + "." + FilenameUtils.getExtension(presentationURL));
					FileUtils.copyFile(new File(presentationURL), tempPPTPath);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for copying the ppt: " + (new_time - current_time));
					current_time = new_time;

					keepSlide(tempPPTPath.getAbsolutePath(), i);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for poi to seperate the prsentation is: " + (new_time - current_time));
					current_time = new_time;
				} else {
					System.out.println("Alert caught");
					break;
				}

			}

		} else if (presentationURL.endsWith(".ppt")) {

			HSLFSlideShow ppt = new HSLFSlideShow(out);
			System.out.println("Creating slides");
			List<HSLFSlide> slides = ppt.getSlides();

			String presentationName = FilenameUtils.getBaseName(presentationURL);
			current_time = System.currentTimeMillis();
			for (int i = 0; i < slides.size(); i++) {
				if (!Call.workspace.cancelled) {
					File tempPPTPath = new File(tempPath,
							presentationName + "." + i + "." + FilenameUtils.getExtension(presentationURL));
					FileUtils.copyFile(new File(presentationURL), tempPPTPath);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for copying the ppt: " + (new_time - current_time));
					System.out.println("\nTime taken for copying the ppt: " + (new_time - current_time));
					current_time = new_time;

					keepSlide(tempPPTPath.getAbsolutePath(), i + 1);
					new_time = System.currentTimeMillis();
					bw.write("\nTime taken for poi to seperate the prsentation is: " + (new_time - current_time));
					System.out.println(
							"\nTime taken for poi to seperate the prsentation is: " + (new_time - current_time));
					current_time = new_time;
				} else {
					System.out.println("Alert caught");
					break;
				}

			}
		}
		// ppt.close();
		bw.close();
		Future<String> v = new Future<String>() {

			public boolean isDone() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}

			public String get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				// TODO Auto-generated method stub
				return null;
			}

			public String get() throws InterruptedException, ExecutionException {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean cancel(boolean mayInterruptIfRunning) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		return v;
	}

	public static void keepSlide(String presentationPath, int index) {
		File file = new File(presentationPath);
		try {
			if (presentationPath.endsWith(".pptx")) {
				XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
				System.out.println(ppt.getSlides().size());
				List<XSLFSlide> slides = ppt.getSlides();
				XSLFSlide selectesdslide = slides.get(index);
				ppt.setSlideOrder(selectesdslide, 0);
				FileOutputStream out = new FileOutputStream(file);
				ppt.write(out);
				out.close();
				ppt = new XMLSlideShow(new FileInputStream(file));
				slides = ppt.getSlides();
				int i = slides.size() - 1;
				while (i > 0) {
					ppt.removeSlide(i);
					i--;
				}
				out = new FileOutputStream(file);
				// Saving the changes to the presentation
				System.out.println(ppt.getSlides().size());
				ppt.write(out);
				out.close();
			} else if (presentationPath.endsWith(".ppt")) {
				HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(file));
				// System.out.println(ppt.getSlides().size());
				List<HSLFSlide> slides = ppt.getSlides();
				ppt.reorderSlide(index, 1);

				// ppt.setSlideOrder(selectesdslide, 0);
				FileOutputStream out = new FileOutputStream(file);
				System.out.println("Yes");

				ppt.write(out);

				out.close();
				ppt = new HSLFSlideShow(new FileInputStream(file));
				slides = ppt.getSlides();
				System.out.println(slides.size());
				int i = slides.size() - 1;
				while (i > 0) {
					ppt.removeSlide(i);
					i--;
				}
				out = new FileOutputStream(file);
				// Saving the changes to the presentation
				System.out.println(ppt.getSlides().size());
				ppt.write(out);
				out.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getPreviewImage(String videoPath) {
		FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper();
		String previewPath = ffmpegWrapper.getPreview(videoPath);
		return previewPath;
	}
}
