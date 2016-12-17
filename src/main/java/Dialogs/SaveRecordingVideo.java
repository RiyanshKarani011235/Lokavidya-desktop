package Dialogs;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;

import org.apache.commons.lang.RandomStringUtils;

import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.Video;
import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.operations.SegmentService;
import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.GeneralUtils;

import Dialogs.OpenAndroid.ProgressDialog;
import Xuggler.DecodeAndSaveAudioVideo;
import gui.Call;
import gui.WorkspaceUIHelper;

public class SaveRecordingVideo {
	
	class Task extends SwingWorker<Void, Void> {

		public Project project;
		
		@Override
		protected Void doInBackground() throws Exception {
			
			run_();
			frame.dispose();
			return null;
			 
		}
		
		public void run_() {
			List<Segment> slist = project.getOrderedSegmentList();
			for(Segment s:slist){
				if(s.getSlide()!=null) {
					// consider only the slide that has been altered (slide recording)
					System.out.println("ironstein - I am iron man");
					if(s.getSlide().getTempAudioURL()!=null && s.getSlide().getTempMuteVideoURL()!=null){
						
						// saving temp video as an flv file in lokavidya folder
						File originalTempVideo = new File(s.getSlide().getTempMuteVideoURL());
						System.out.println("originalTempVideo : Saving at : " + originalTempVideo.getAbsolutePath());
						
						// stitching audio and video files, and generating a flv file as output
						File tempVideo = new File(project.getProjectURL(), RandomStringUtils.randomAlphanumeric(10).toLowerCase()+".flv");
						System.out.println("tempVideo : Saving at : " + tempVideo.getAbsolutePath());
						System.out.println(originalTempVideo.getAbsolutePath());
						System.out.println(s.getSlide().getTempAudioURL());
						System.out.println(tempVideo.getAbsolutePath());
						DecodeAndSaveAudioVideo.stitch(originalTempVideo.getAbsolutePath(),s.getSlide().getTempAudioURL(),tempVideo.getAbsolutePath());
						System.out.println("wait ...");
						
						// converting tempVideo, and saving it to globalVideo.getVideoURL() path
						// ffmpeg -i inputVideo.flv -c:v libxvid -c:a aac -strict experimental outputVideo.mp4
						Video globalVideo = new Video(project.getProjectURL());
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
			
			ProjectService.persist(project);
			Call.workspace.removeTimeline();
			Call.workspace.populateTimeline();
			WorkspaceUIHelper.disableStop();
		}
	}
	
	public JFrame frame;
	public String pathDef;
	public static String path;
	private JButton btnNewButton_1;
	public JLabel lblNewLabel;
	
	public JProgressBar progressBar;
	public JPanel innerPanel;
	private JButton btnCancel;
	private JLabel lblNewLabel1;
	public static void run(Project project) {
		
		System.out.println("SaveRecordingArea.run() called");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("new SaveRecordingVideo");
					Call.workspace.cancelled = false;
					SaveRecordingVideo window = new SaveRecordingVideo(project);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public SaveRecordingVideo(Project project) {
		System.out.println("initializing project");
		initialize(project);
	}
	
	public void initialize(Project project) {
		frame = new JFrame();
		frame.setBounds(100, 100, 542, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		lblNewLabel = new JLabel("SAVE VIDEO");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel);

		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); 
        progressBar.setIndeterminate(true);
      
        innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.add(progressBar);
        innerPanel.setSize(400, 30);
        innerPanel.setVisible(false);
        innerPanel.setOpaque(true);
        
        
        lblNewLabel1 = new JLabel("Save video?");
        innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
        //innerPanel.setVisible(false);
		frame.getContentPane().add(innerPanel);
		
		btnNewButton_1 = new JButton("Save");
		springLayout.putConstraint(SpringLayout.NORTH, innerPanel, 0, SpringLayout.NORTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 27, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -28, SpringLayout.SOUTH, frame.getContentPane());
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Task task = new Task();
				task.project = project;
				task.execute();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		frame.getContentPane().add(btnNewButton_1);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Call.workspace.cancelled = true;
				// do something
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, innerPanel);
		springLayout.putConstraint(SpringLayout.WEST, btnCancel, 6, SpringLayout.EAST, btnNewButton_1);
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		// disable cancel button - ironstein - 22-11-16
		frame.getContentPane().add(btnCancel);
	}
	
}
