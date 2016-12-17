
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
//import com.sun.star.awt.MouseEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
//import com.sun.star.awt.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
//import com.sun.star.awt.MouseEvent;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import libreoffice.LibreDesktop;

import org.apache.commons.io.FilenameUtils;
//import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.iitb.lokavidya.core.data.State;

import Dialogs.CreateProject;
import Dialogs.DirectoryChooser;
import Dialogs.Feedback;
import Dialogs.InstallationInstructions;
import Dialogs.JTimer;
//import Dialogs.Feedback;
import Dialogs.OpenAndroid;
import Dialogs.OpenAndroidExp;
import Dialogs.OpenPdf;
import Dialogs.OpenPresentation;
import Dialogs.OpenProject;
import Dialogs.OpenVideo;
import Dialogs.SelectArea;
import Dialogs.VideoFormat;


import com.iitb.lokavidya.core.data.Project;
import com.iitb.lokavidya.core.data.Segment;
import com.iitb.lokavidya.core.data.State;
import com.iitb.lokavidya.core.data.Video;
import com.iitb.lokavidya.core.data.Segment.SegmentType;
import com.iitb.lokavidya.core.operations.ProjectOperations;
import com.iitb.lokavidya.core.operations.ProjectService;
import com.iitb.lokavidya.core.operations.SegmentService;
import com.iitb.lokavidya.core.utils.FFMPEGWrapper;
import com.iitb.lokavidya.core.utils.GeneralUtils;
import com.iitb.lokavidya.core.utils.UIUtils;

import java.awt.Point;
import java.awt.Rectangle;

public class Workspace extends JFrame implements WindowListener,
WindowFocusListener,
WindowStateListener {

	public JPanel contentPane, presentationInnerPanel, outputInnerPanel, timelineInnerPanel, videosInnerPanel;
	public JInternalFrame notesFrame, explorerFrame, timelineFrame, slideFrame;
	public JComponent presentationPanel;
	private JComponent outputPanel;
	private JComponent videosPanel;
	private JMenuBar menuBar;
	public RecordingFrame recFrame = null;
	private JMenu mnFile, mnEdit, mnOptions, mnExport, mnImport, mnHelp;
	private JMenuItem mntmNewMenuItem, mntmOpenMenuItem, mntmEditPres, mntmVideoFormat,
			mntmFormat, mntmAndroidexp, mntmPresentation, mntmVideo, mntmAndroid, 
			mntmFeedback, mntmInstallationInstructions, mntmPdf;
	public JCheckBoxMenuItem  mntmDecideRecordingArea;
	private JPanel panel,StitchToolbarpanel,innerPanel;
	private JTabbedPane tabbedPane;
	public JButton btnRecord,btnDelete,btnRefresh;
	public String path, location, name;
	public ArrayList<CustomPanel> customPanelList; 		
	public XMLSlideShow currentPptx;
	public SlideShow currentPpt;
	public List<XSLFSlide> currentPptSlide;
	public int x = 0, y = 0, recordingWidth = 0, recordingHeight = 0,videoWidth=800,videoHeight=600;
	private String pptPath;

	public boolean continuous = true;
	public static boolean paused=false;
	public boolean pausedFile=false;
	public static boolean screenRecordingFlag=false;
	public static boolean playing=false;
	public List<File> deleteList;
	public int framerate=3;
	private static JLabel lblSlideDisplay;
	int currentSlideNumber = 0;
	public static LibSlide currentSlide=null;
	public JButton stopbtn;
	public JButton btnNext;
	private JScrollPane scrollPane;
	private JScrollPane timelineScroll;
	public JButton btnSaveOrder;
	public JButton btnStitch, btnSelectAll,btnUnselectAll;
	public JButton btnInsert;
	public JButton btnDiscard;
	public JButton btnAdd;
	public static Project currentProject;
	public static Segment currentSegment;
	public List<Segment> refreshList;
	private static ProjectOperations po;
	public JButton btnScreenRec;
	public JTextArea notesArea;
	public JLabel notesLabel_1,notesLabel_2;
	public JButton btnNewButton;
	private JButton btnNewButton_1;
	private String pathDef;
	private JTextField textField;
	public JMenuItem mntmClose;
	public static int initialDragID;
	static int autoscrollMargin = 43;
	public boolean cancelled=false;
	public Stack<State> executedActions,redoActions;
	public State currentState; 
	private JButton btnNewButton_2;
	private JLabel label_1;
	/* Progress Bar */
	public ProgressDialog  dialog;
	public int progressBarFlag=0;
	public int selectAllFlag = 0;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	
	private int height,width,upperHeight,quarterWidth,padding=5;
	private int margin;
	
    public JLabel timeDisplayLabel;
	/**
	 * Launch the application.
	 */
	
	public void initializeStates()
	{
		executedActions.clear();
		redoActions.clear();
		currentState=new State(currentProject);
		executedActions.push(currentState);
		currentState=new State();
	}
	public void startOperation() {
		currentState=new State();
	}
	
	public void endOperation()
	{
		ProjectService.persist(currentProject);
		currentState.setChangedState(currentProject.copyOrderingSequence());
		executedActions.push(currentState);
		currentState=null;
		redoActions.clear();
		if(!mntmUndo.isEnabled())
			mntmUndo.setEnabled(true);
	}
	public void cancelOperation()
	{
		currentState=null;
		System.exit(0);
	}			
	public void Undo()
	{
		redoActions.push(executedActions.pop());
		State temp=executedActions.pop();
		currentProject.setOrderedSegmentList(temp.getChangedState());
		executedActions.push(temp);
		ProjectService.persist(currentProject);
		mntmRedo.setEnabled(true);
		if(executedActions.empty())
			mntmUndo.setEnabled(false);
		repopulateProject();
	}
	public void Redo()
	{
		currentProject.setOrderedSegmentList(redoActions.peek().getChangedState());
		ProjectService.persist(currentProject);
		executedActions.push(redoActions.pop());
		mntmUndo.setEnabled(true);
		if(redoActions.empty())
			mntmRedo.setEnabled(false);
		repopulateProject();
	}
	public void changeSlideRight() {
		try{
			paused=false;
			WorkspaceUIHelper.disableRecord();
			List <Segment> segmentList = currentProject.getOrderedSegmentList();
			int position = segmentList.indexOf(currentSegment);
			if ((position+1) != segmentList.size()) {
				
				if(screenRecordingFlag)
				{
				//	Thread.sleep(1600);
					po.stopToggleSlideRecording(currentProject);
					setPreview(Integer.toString(position+1));
					Thread.sleep(300);
					po.startToggleSlideRecording(currentProject, currentSegment);
				}
				else
				{
				//	Thread.sleep(1600);
					ProjectOperations.stopAudioToggleRecording();
					setPreview(Integer.toString(position+1));
					Thread.sleep(300);
					ProjectOperations.startAudioRecording(currentProject, currentSegment);
				}
			}
			else {
				stopRecording();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
/*
	public static void changeSlideLeft() {
		for (Slide libraryslide : librarySlides) {
			if (libraryslide.getName().equals(currentSlide.getName())) {
				int index = librarySlides.indexOf(libraryslide);
				if ((index - 1) >= 0) {
					setPreview(librarySlides.get(index - 1).getName());
					recording.stopSlide();
					recording.startSlide();
				} else {
					recording.stopSlide();
				}
				break;
			}
		}

	}*/

	public void showOutput() {
		tabbedPane.setSelectedIndex(2);
		
	}
	public void initCustomPanel() {
		CustomPanel.highlightCount =0;
		CustomPanel.slideCount=0;
		customPanelList.clear();
	}

	
	public void makeExplorerVideo(final Video v) {
		
		// Add to individualrecordings
		String vname=FilenameUtils.getBaseName(v.getVideoURL());
		JLabel l = new JLabel(vname);
		l.setName(vname);
		l.setIcon(createImageIcon("resources/individual_video.png"));
		l.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String f = e.getComponent().getName();
				File stitched = new File(v.getVideoURL());
				GeneralUtils.openFile(stitched);
			}
		});
		videosInnerPanel.add(l);
	}

	public void populateExplorerVideos()
	{
		 List<Segment> slist = currentProject.getOrderedSegmentList();
		for(Segment s:slist)
		{
			if(s.getVideo()!=null)
			{
				makeExplorerVideo(s.getVideo());
			}
		}
		videosPanel.revalidate();
		videosPanel.repaint();
	}

	public void populateExplorerOutput() {

		if (currentProject.getOrderingSequence().size()>0) {
			String img=currentProject.getSlideSegment(0).getSlide().getImageURL();
			if (new File(img).exists()) { // Show in explore
				JLabel l = new JLabel(currentProject.getProjectName());
				l.setName(currentProject.getProjectName());

				l.setIcon(UIUtils.getThumbnail(img));
				l.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (Desktop.isDesktopSupported()) {

							String f = e.getComponent().getName();
							String fileoutput = new File(currentProject
									.getProjectURL(), currentProject
									.getProjectName() + ".mp4").getAbsolutePath();
							File stitched = new File(fileoutput);
							if (stitched.exists()) {
								Desktop desktop = Desktop.getDesktop();
								try {
									desktop.open(stitched);
								} catch (java.io.IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}

					}
				});
				outputInnerPanel.add(l);
				outputPanel.revalidate();
				outputPanel.repaint();
			}
		}
	}

	public void populateExplorerSlides() {
		currentProject = ProjectService.getInstance(currentProject.getProjectJsonPath());
		List<Segment> slist = currentProject.getOrderedSegmentList();
		System.out.println("Retrieved ordered explorer slide list");
		int displayIndex=0;
		for(Segment s:slist)
		{
			System.out.println("Found explorer slide "+displayIndex);
			if(s.getSlide()!=null)
			{
				System.out.println("Slide is not null");
				addExplorerSlide(s.getSlide(),displayIndex);
				displayIndex++;
			}
		}
		presentationPanel.revalidate();
		presentationPanel.repaint();
	}
	

	public void createProject() {
		clean();
		populateProject();
	}

	public void repopulateProject() {
		clean();
		populateProject();
		GeneralUtils.stopOfficeInstance();
	}
	
	public void populateProject() {
		System.out.println("Calling populate");
		populateExplorer();
		populateTimeline();

	}

	public void populateExplorer() {
		populateExplorerSlides();
		populateExplorerVideos();
		populateExplorerOutput();
	}

	public void populateTimeline() {
		currentProject = ProjectService.getInstance(currentProject.getProjectJsonPath());
		List<Segment> slist = currentProject.getOrderedSegmentList();
		System.out.println("sList size: "+slist.size());
		customPanelList.clear();
		int slideIndex=0,videoIndex=0;
		
		for(Segment s:slist) {
			if(s.getSlide()!=null) {
				JComponent p=makeTimelineSlide(s.getSlide(),slideIndex,s);
				timelineInnerPanel.add(p);
				slideIndex++;
			} else if(s.getVideo()!=null) {
				JComponent p=makeTimelineVideo(s.getVideo(),videoIndex,s);
				timelineInnerPanel.add(p);	
				videoIndex++;
			}
		}
		timelineFrame.getContentPane().revalidate();
		timelineFrame.getContentPane().repaint();
	}

	private int roundUp(int n) {
		if ((n % 2) != 0) {
			return (n + 1);
		}
		return n;
	}

	public void setCoordinates(int rectx, int recty, int rectwidth, int rectheight) {
		x = roundUp(rectx);
		y = roundUp(recty);
		recordingWidth = rectwidth;
		recordingHeight = rectheight;
	}
	
	public void resetCoordinates() {
		System.out.println("Resetting coordinates");
		x = 0;
		y = 0;	
		recordingWidth = 0;
		recordingHeight = 0;
	}
    
	public void setinitialDragID(int initialDragID){
    	Workspace.initialDragID = initialDragID;
    }
    
    public int getinitialDragID(){
    	return Workspace.initialDragID;
    }
    
	public void disable() {
		notesFrame.setEnabled(false);
		explorerFrame.setEnabled(false);
		timelineFrame.setEnabled(false);
		slideFrame.setEnabled(false);
		presentationPanel.setEnabled(false);
		// mnEdit.setEnabled(false);
		mnImport.setEnabled(false);
		 mnOptions.setEnabled(false);
		mnExport.setEnabled(false);
		mntmVideoFormat.setEnabled(false);
		// mntmAudioFormat.setEnabled(false);
		mntmDecideRecordingArea.setEnabled(false);
		btnRecord.setEnabled(false);
		btnScreenRec.setEnabled(false);
		stopbtn.setEnabled(false);
		
		btnNext.setEnabled(false);
		btnDiscard.setEnabled(false);
		mntmFormat.setEnabled(false);
		btnRefresh.setEnabled(false);
		btnDelete.setEnabled(false);
		btnStitch.setEnabled(false);
		btnSelectAll.setEnabled(false);
		btnAdd.setEnabled(false);
		btnUnselectAll.setEnabled(false);
	}

	public void enable() {
		// System.out.print(b);
		notesFrame.setEnabled(true);
		explorerFrame.setEnabled(true);
		timelineFrame.setEnabled(true);
		slideFrame.setEnabled(true);
		presentationPanel.setEnabled(true);
		//mnEdit.setEnabled(true);
		mnImport.setEnabled(true);
		mnOptions.setEnabled(true);
		mnExport.setEnabled(true);
		mntmVideoFormat.setEnabled(true);
		//mntmAudioFormat.setEnabled(true);
		mntmDecideRecordingArea.setEnabled(true);
		btnRecord.setEnabled(true);
		btnScreenRec.setEnabled(true);
		
		btnRefresh.setEnabled(true);
		btnDelete.setEnabled(true);
		btnStitch.setEnabled(true);
		btnSelectAll.setEnabled(true);
		btnAdd.setEnabled(true);
		btnUnselectAll.setEnabled(true);
	}

	private void launchPresentation(String pptPath) {

		try {

			File rootPath = new File(System.getProperty("java.class.path"));
			System.out.println("The root path is " + rootPath);
			String openOfficePath = GeneralUtils.findOooPath();
			if (openOfficePath!=null) {
				LibreDesktop.launchOfficeInstance(pptPath, openOfficePath);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Workspace frame = new Workspace();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void addExplorerSlide(com.iitb.lokavidya.core.data.Slide slide,int index) {
		
		JLabel label = new JLabel();

		label.setName(Integer.toString(index));
		label.setIcon(UIUtils.getThumbnail(slide.getImageURL()));

		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					paused = false;
					currentSlideNumber = Integer.parseInt(e.getComponent().getName());
					if(btnNext.isEnabled()){
						System.out.println("Found " + e.getComponent().getName());
						
						if(screenRecordingFlag) {
							WorkspaceUIHelper.disableRecord();
							//Thread.sleep(1600);
							po.stopToggleSlideRecording(currentProject);
							setPreview(e.getComponent().getName());
							Thread.sleep(300);
							po.startToggleSlideRecording(currentProject, currentProject.getSlideSegment(currentSlideNumber));
						} else if(playing) {
							WorkspaceUIHelper.disableRecord();
							//Thread.sleep(1600);
							ProjectOperations.stopAudioToggleRecording();
							setPreview(e.getComponent().getName());
							Thread.sleep(300);
							ProjectOperations.startAudioRecording(currentProject, currentProject.getSlideSegment(currentSlideNumber));
						}
					} else setPreview(e.getComponent().getName());
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}	
			
		});
		presentationInnerPanel.add(label);
		presentationPanel.revalidate();
		presentationPanel.repaint();
	}

	public void removeExplorerSlides() {
		Component[] a = presentationInnerPanel.getComponents();
		for (Component j : a) {
			presentationInnerPanel.remove(j);
		}
	}

	public void removeExplorerVideos() {
		// Remove explorer videos
		Component[] a = videosInnerPanel.getComponents();
		for (Component j : a) {
			videosInnerPanel.remove(j);
		}
		// remove from individual recordings
		
		
	}

	public void removeExplorerOutput() {
		Component[] a = outputInnerPanel.getComponents();
		for (Component j : a) {
			outputInnerPanel.remove(j);
		}
	}

	public void clean() {
		removeExplorer();
		removeTimeline();
	}

	public void removeExplorer() {
		removeExplorerSlides();
		removeExplorerVideos();
		removeExplorerOutput();
	}

	public void removeTimeline() {
		Component[] a = timelineInnerPanel.getComponents();
		for (Component j : a) {
			timelineInnerPanel.remove(j);
		}
	
	}

	public void setPreview(String slidename) {
		int index=Integer.parseInt(slidename);
		System.out.println("Showing slide "+index);
		Segment s=currentProject.getSlideSegment(index);
		
		Segment oldSegment=currentSegment;
		currentSegment =s;
		
		lblSlideDisplay.setIcon(UIUtils.getPreview(s.getSlide().getImageURL(),lblSlideDisplay.getWidth(),lblSlideDisplay.getHeight()));
		String duration="--:--";
		if(s.getSlide().getAudio()!=null || s.getVideo()!=null){
			duration = GeneralUtils.convertToMinSecFormat(s.getTime());
		}
		 Call.workspace.timeDisplayLabel.setText(duration);
		if(oldSegment!=null)
			customPanelList.get(currentProject.indexOf(oldSegment)).stopPreview();
		highlightCurrent();
		Call.workspace.revalidate();
		Call.workspace.repaint();
	}

	public void highlightCurrent() {
		if (currentSegment!=null) {
			customPanelList.get(currentProject.indexOf(currentSegment))
					.setPreview();
			Call.workspace.timelineFrame.revalidate();
			Call.workspace.timelineFrame.repaint();
		}
	}

	protected static ImageIcon createImageIcon(String path) {
		System.out.println("path : " + path);
		java.net.URL imgURL = Workspace.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}

		ImageIcon p = null;
		URL l = Workspace.class.getResource(path);
//		String l = new File(path).getAbsolutePath();

		if(l != null && new File(l.toString()).exists()) {
			System.out.println("found file");
		}

		if(l!=null)
		{
			p=new ImageIcon(l);
			System.out.println("Found image resource");
			System.out.println(p.toString());
			return p;
		} else {
			System.out.println("Did not find image resource");
			if(new File(path).exists()){
				return new ImageIcon(path);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}
	}

	protected JComponent makePresPanel() {
		presentationInnerPanel = new JPanel(false);
		presentationInnerPanel.setBackground(new Color(245, 245, 220));
		presentationInnerPanel.setLayout(new GridLayout(0, 1, 10, 10));

		JScrollPane scrollPane = new JScrollPane();
		// presentationPanel.add(scrollPane);
		scrollPane.setViewportView(presentationInnerPanel);
		scrollPane.setVisible(true);
		// panel.add(filler);
		return scrollPane;
	}

	protected JComponent makeVideosPanel() {
		videosInnerPanel = new JPanel(false);
		videosInnerPanel.setBackground(new Color(245, 245, 220));
		videosInnerPanel.setLayout(new GridLayout(0, 1, 10, 10));

		JScrollPane scrollPane = new JScrollPane();
		// presentationPanel.add(scrollPane);
		scrollPane.setViewportView(videosInnerPanel);
		scrollPane.setVisible(true);
		// panel.add(filler);
		return scrollPane;
	}

	public JComponent makeTimelineSlide(com.iitb.lokavidya.core.data.Slide slide, int index, Segment segment) {
	//	JPanel piece = new JPanel(false);
		System.out.println("Slide Url: "+slide.getImageURL());
		CustomPanel piece = new CustomPanel(segment);
        String slidename="Slide "+Integer.toString(index+1);
      
        JLabel l1=new JLabel(slidename);
        piece.springLayout.putConstraint(SpringLayout.NORTH, l1, 5, SpringLayout.NORTH, this);
        piece.springLayout.putConstraint(SpringLayout.WEST, l1, 7, SpringLayout.WEST, this);
        //piece.springLayout.putConstraint(SpringLayout.SOUTH, l1, 34, SpringLayout.NORTH, this);
        piece.springLayout.putConstraint(SpringLayout.EAST, l1, 133, SpringLayout.WEST, this);
        JLabel l2=new JLabel();
        l2.setName(Integer.toString(index));
        System.out.println("Slide image URL: "+slide.getImageURL());
        l2.setIcon(UIUtils.getThumbnail(slide.getImageURL()));
        l2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					paused=false;
					currentSlideNumber = Integer.parseInt(e.getComponent().getName());
					if(btnNext.isEnabled()){
						
						if(screenRecordingFlag)
						{
							WorkspaceUIHelper.disableRecord();
							//Thread.sleep(1600);
							po.stopToggleSlideRecording(currentProject);
							setPreview(e.getComponent().getName());
							Thread.sleep(300);
							po.startToggleSlideRecording(currentProject, currentProject.getSlideSegment(currentSlideNumber));
						}
						else if(playing)
						{
							WorkspaceUIHelper.disableRecord();
							//Thread.sleep(1600);
							ProjectOperations.stopAudioToggleRecording();
							setPreview(e.getComponent().getName());
							Thread.sleep(300);
							ProjectOperations.startAudioRecording(currentProject, currentProject.getSlideSegment(currentSlideNumber));
						}
					}
					else
						setPreview(e.getComponent().getName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
        piece.springLayout.putConstraint(SpringLayout.NORTH, l2, 4, SpringLayout.SOUTH, l1);
        piece.springLayout.putConstraint(SpringLayout.WEST, l2, 0, SpringLayout.WEST, l1);
        JButton b3=new JButton("Edit slide");
        b3.setBackground(SystemColor.control);
        b3.setFont(new Font("Tahoma", Font.PLAIN, 10));
        b3.setPreferredSize(new Dimension(10, 10));
        b3.setName(Integer.toString(index));
        //b3.setMinimumSize(new Dimension(15, 28));
        piece.springLayout.putConstraint(SpringLayout.NORTH, b3, -27, SpringLayout.SOUTH, b3);
        piece.springLayout.putConstraint(SpringLayout.WEST, b3, 0, SpringLayout.WEST, l1);
        piece.springLayout.putConstraint(SpringLayout.EAST, b3, -5, SpringLayout.EAST, l1);
        piece.springLayout.putConstraint(SpringLayout.SOUTH, b3, -10, SpringLayout.SOUTH, piece);
        b3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("Segment slide is "+e.getComponent().getName());
				Segment s=currentProject.getSlideSegment(Integer.parseInt(e.getComponent().getName()));
				SegmentService.convertPresentation(s, currentProject);
				GeneralUtils.stopOfficeInstance();
				launchPresentation(s.getSlide().getPptURL());
				refreshList.add(s);
			}
		});
        JLabel l4=new JLabel();
        l4.setName("Audio");
        l4.setFont(new Font("Tahoma", Font.PLAIN, 10));
        piece.springLayout.putConstraint(SpringLayout.NORTH, l4, 0, SpringLayout.NORTH, l2);
        piece.springLayout.putConstraint(SpringLayout.EAST, l4, +85, SpringLayout.EAST, l2);
        
        JButton b5=new JButton("Play");
        b5.setName("Play+"+Integer.toString(index));
        b5.setBackground(SystemColor.control);
        b5.setFont(new Font("Tahoma", Font.PLAIN, 10));
        piece.springLayout.putConstraint(SpringLayout.NORTH, b5, 10, SpringLayout.SOUTH, l4);
        piece.springLayout.putConstraint(SpringLayout.WEST, b5, -63, SpringLayout.EAST, l4);
        piece.springLayout.putConstraint(SpringLayout.EAST, b5, 0, SpringLayout.EAST, l4);
        
        b5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String str=e.getComponent().getName().replace("Play+", "");
				Segment s=currentProject.getSlideSegment(Integer.parseInt(str));
				if(s.getSlide().getAudio()!=null)
				{
					File f=new File(s.getSlide().getAudio().getAudioURL());
					GeneralUtils.openFile(f);
				}
				else if(s.getVideo()!=null)
				{
					File f=new File(s.getVideo().getVideoURL());
					GeneralUtils.openFile(f);
				}
			}
		});
        JButton b6=new JButton("Delete");
        b6.setName(Integer.toString(index));
        b6.setBackground(SystemColor.control);
        b6.setFont(new Font("Tahoma", Font.PLAIN, 10));
        piece.springLayout.putConstraint(SpringLayout.NORTH, b6, 6, SpringLayout.SOUTH, b5);
        piece.springLayout.putConstraint(SpringLayout.WEST, b6, -63, SpringLayout.EAST, l4);
        piece.springLayout.putConstraint(SpringLayout.EAST, b6, 0, SpringLayout.EAST, l4);
        b6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				 startOperation();
				 System.out.println("Registered delete click");
				 Container parent = e.getComponent().getParent();
				 for(Component x:parent.getComponents())
				 {
					 try {
						if (x.getName().equals("Audio")) {
							((JLabel) x)
									.setIcon(createImageIcon("resources/mic_off.png"));
							((JLabel) x).setText("No audio");
						}
						if (x.getName().contains("Play")) {
							x.setEnabled(false);
						}
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				 }
				 e.getComponent().setEnabled(false);
				 parent.revalidate();
				 parent.repaint();
				Segment s=currentProject.getSlideSegment(Integer.parseInt(e.getComponent().getName()));
				
				SegmentService.softDeleteAudio(currentProject,s);
				SegmentService.softDeleteVideo(currentProject,s);
				endOperation();
			}
		});
        
        
        if(slide.getAudio()!=null) {
        	System.out.println("Could find slide audio "+slide.getAudio().getAudioURL());
        	l4.setIcon(createImageIcon("resources/mic.png"));
        	l4.setText("Has Audio");
        	b5.setEnabled(true);
        	b6.setEnabled(true);
        } else if(segment.getVideo()!=null) {
        	l4.setIcon(createImageIcon("resources/slidevideo.png"));
        	l4.setText("Has Video");
        	b5.setEnabled(true);
        	b6.setEnabled(true);
        } else {
    		System.out.println("Couldn't find slide audio");
        	l4.setIcon(createImageIcon("resources/mic_off.png"));
        	l4.setText("No audio");
        	b5.setEnabled(false);
        	b6.setEnabled(false);
        	
        	//l3.setFont(font);
    	}
 
        piece.add(l1);
        piece.add(l2);
        piece.add(l4);
        piece.add(b5);
        piece.add(b6);
 
        // if PDF imported, then there is no presentation for that slide
        if(!(segment.getSlide().getPptURL() == null)) {
        	piece.add(b3);
        }
        //piece.setName(slidename);
    	
        
        customPanelList.add(piece);
        return piece;
	}

	protected JComponent makeTimelineVideo(com.iitb.lokavidya.core.data.Video v,int index, Segment segment) {
		CustomPanel piece = new CustomPanel(segment);

		JLabel l1 = new JLabel("Video "+Integer.toString(index+1));
		JLabel l2 = new JLabel();
		System.out.println("GOING FOR PREVIEW " + segment.getVideo().getVideoURL());
		String imagePath = ProjectService.getPreviewImage(segment.getVideo().getVideoURL());
		System.out.println(imagePath+" "+ segment.getVideo().getVideoURL());
		l2.setIcon(UIUtils.getThumbnail(imagePath));
		JButton b5=new JButton("Play");
        b5.setName(Integer.toString(index));
        b5.setBackground(SystemColor.control);
        b5.setFont(new Font("Tahoma", Font.PLAIN, 10));
        piece.springLayout.putConstraint(SpringLayout.NORTH, b5, 38, SpringLayout.SOUTH,l1 );
        piece.springLayout.putConstraint(SpringLayout.WEST, b5, 4, SpringLayout.EAST,l1);
        piece.springLayout.putConstraint(SpringLayout.EAST, b5, 85, SpringLayout.EAST, l2);
        
        b5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String str=e.getComponent().getName();
				Segment s=currentProject.getVideo(Integer.parseInt(str));
				if(s.getVideo()!=null)
				{
					File f=new File(s.getVideo().getVideoURL());
					if(f.exists())
						GeneralUtils.openFile(f);
				}
				
			}
		});
		
		piece.springLayout.putConstraint(SpringLayout.NORTH, l1, 5, SpringLayout.NORTH, this);
        piece.springLayout.putConstraint(SpringLayout.WEST, l1, 7, SpringLayout.WEST, this);
        //piece.springLayout.putConstraint(SpringLayout.SOUTH, l1, 34, SpringLayout.NORTH, this);
        piece.springLayout.putConstraint(SpringLayout.EAST, l1, 133, SpringLayout.WEST, this);
        
        piece.springLayout.putConstraint(SpringLayout.NORTH, l2, 4, SpringLayout.SOUTH, l1);
        piece.springLayout.putConstraint(SpringLayout.WEST, l2, 0, SpringLayout.WEST, l1);

        piece.add(l1);
        piece.add(l2);
        piece.add(b5);
        customPanelList.add(piece);
		return piece;
	}

	

	protected JComponent makeOutputPanel() {
		outputInnerPanel = new JPanel(false);
		outputInnerPanel.setBackground(new Color(245, 245, 220));
		outputInnerPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		// presentationPanel.add(scrollPane);
		scrollPane.setViewportView(outputInnerPanel);
		scrollPane.setVisible(true);
		// panel.add(filler);
		return scrollPane;
	}

	public void startScreenRecording() {
		System.out.println("Log : start recording");
		po = new ProjectOperations();
		po.startToggleSlideRecording(currentProject, currentSegment);
		screenRecordingFlag = true;
		WorkspaceUIHelper.disableRecord();
		recFrame = new RecordingFrame(x, y, recordingWidth,
						recordingHeight);
		recFrame.showFrame();
	}

	public void stopRecording() {
		System.out.println("Log : stop recording");
		paused=false;
		if(screenRecordingFlag) {
			// Stop screen recording
			System.out.println("stopping screen recording");
			screenRecordingFlag=false;
			po.stopToggleSlideRecording(currentProject);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			ProjectOperations.stopSlideRecording(currentProject);
			recFrame.hideFrame();
		} else{
			// Stop audio recording
			System.out.println("stopping audio recording");

			ProjectOperations.stopAudioToggleRecording();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			playing=false;
			ProjectOperations.stopAudioRecording(currentProject);

			ProjectService.persist(currentProject);
			Call.workspace.removeTimeline();
			Call.workspace.populateTimeline();
			WorkspaceUIHelper.disableStop();
		}

	}
	
	public void discardRecording() {
		System.out.println("Log : discard recording");
		paused=false;
		if(screenRecordingFlag)
		{
			screenRecordingFlag=false;
			po.discardToggleSlideRecording(currentProject);
			recFrame.hideFrame();
			
		}
		else
		{
			ProjectOperations.discardSlideRecording(currentProject);
			playing=false;
		}
		ProjectService.persist(currentProject);
		Call.workspace.removeTimeline();
		Call.workspace.populateTimeline();
		WorkspaceUIHelper.disableStop();
	}
	/**
	 * Create the frame.
	 */
	public Workspace() {
		//recording = new Recording();
		// selfRef=this;
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		Properties p = System.getProperties();
		
		 addWindowListener(this);
	     addWindowFocusListener(this);
	     addWindowStateListener(this);
	        
		customPanelList= new ArrayList<CustomPanel>();
		refreshList=new ArrayList<Segment>();
		deleteList=new ArrayList<File>();
		executedActions=new Stack<State>();
		redoActions=new Stack<State>();
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit t=getToolkit();
		
		margin=10;
		height=(int) t.getScreenSize().getHeight();
		height=height-scnMax.bottom-scnMax.top;
		width=(int) t.getScreenSize().getWidth();
		width=width-scnMax.left-scnMax.right;
		upperHeight=(3*height)/5;
		quarterWidth=(width-(4*padding))/4;
		setBounds(10+scnMax.left,0+scnMax.top,width-(2*margin),height);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		/*File Menu */
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
        //New Project button
		mntmNewMenuItem = new JMenuItem("New Project");
		mnFile.add(mntmNewMenuItem);
		mntmNewMenuItem.setToolTipText("Create an new project");
		mntmNewMenuItem.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Remove later: CreateProject.workspacestatic = selfRef;
				initCustomPanel();                                            //New Project pop-up invoke
				CreateProject.main(null);
				System.out.println("Enable called");
				// enable();
			}
		});
		//Open Project button
		mntmOpenMenuItem = new JMenuItem("Open Project");
		mnFile.add(mntmOpenMenuItem);
		mntmOpenMenuItem.setToolTipText("Open Existing project from the computer");
		mntmOpenMenuItem.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		mntmOpenMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initCustomPanel();                                           //Open Project pop-up invoke
				try {
					OpenProject.main(null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
			}
		});
		// Exit Button in file
		 mntmClose = new JMenuItem("Close");
		 mntmClose.setAccelerator(KeyStroke.getKeyStroke(
		             KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		 mntmClose.setToolTipText("Exit application");
		 mntmClose.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}

		 });
		mnFile.add(mntmClose);
		    
		/*Edit Menu */   
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mntmUndo.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
		mntmUndo.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent event) {
		            Undo();
		        }
		    });
		mnEdit.add(mntmUndo);
		
		mntmRedo = new JMenuItem("Redo");
		mntmRedo.setEnabled(false);
		mntmRedo.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
		mntmRedo.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent event) {
		            Redo();
		        }
		});
		mnEdit.add(mntmRedo);
		/* Options Menu */
		mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		/* Import Menu */
		mnImport = new JMenu("Import");
		mntmPresentation = new JMenuItem("Presentation ...");
		mntmVideo = new JMenuItem("Video ...");
		mntmAndroid = new JMenuItem("Android Project ...");
		mntmPdf = new JMenuItem("PDF ...");
		mnImport.add(mntmPresentation);
		mnImport.add(mntmVideo);
		mnImport.add(mntmAndroid);
		mnImport.add(mntmPdf);
		menuBar.add(mnImport);

		mntmPresentation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Called open");
				try {
					   customPanelList.clear();
					   CustomPanel.highlightCount=0;
					   CustomPanel.slideCount =0;
					  
					   OpenPresentation.main(null);
					   /*pptPath=new DirectoryChooser(GeneralUtils.getDocumentsPath(),"ppt").selectedfile;
					   System.out.println("Being executed");
					   if(pptPath.equals(""))
						{
							System.out.println("Path null");
							JOptionPane.showMessageDialog(null, "Enter the presentation location", "", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							ProjectService.importPresentation(pptPath,currentProject);
						}*/	
				} catch (NullPointerException ex) {
					// logger.log(Level.SEVERE, e.getMessage(), e);
					ex.printStackTrace();
				}
			}
		});

		mntmVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customPanelList.clear();
				   CustomPanel.highlightCount=0;
				   CustomPanel.slideCount =0;
			
				   OpenVideo.main(null);

			}
		});

		mntmAndroid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customPanelList.clear();
				   CustomPanel.highlightCount=0;
				   CustomPanel.slideCount =0;
			
				OpenAndroid.main(null);
			//	setPaths();

			}
		});

		mntmPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenPdf.main(null);
			}
		});

		mntmVideoFormat = new JMenuItem("Video Format");
//		mnOptions.add(mntmVideoFormat);
		mntmVideoFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VideoFormat.main(null);
			}
		});

		/*
		mntmAudioFormat = new JMenuItem("Audio Format");
		mnOptions.add(mntmAudioFormat);
		*/


		mntmDecideRecordingArea = new JCheckBoxMenuItem("Decide Recording Area");
		mnOptions.add(mntmDecideRecordingArea);
		mntmDecideRecordingArea.setSelected(false);
		mntmDecideRecordingArea.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (mntmDecideRecordingArea.isSelected()) {
					setVisible(false);
					Timer t = new Timer(500 * 1, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// do your reoccuring task

							try {
								SelectArea.main(null);
								mntmDecideRecordingArea.setSelected(true);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					t.start();
					t.setRepeats(false);
				} else {
					resetCoordinates();
				}
			}
		});
		mnExport = new JMenu("Export");
		menuBar.add(mnExport);

		mntmFormat = new JMenuItem("Video Format");
		mnExport.add(mntmFormat);

		mntmAndroidexp = new JMenuItem("Android App");
		mnExport.add(mntmAndroidexp);
		mntmAndroidexp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				OpenAndroidExp.main(null);

			}
		});
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmInstallationInstructions = new JMenuItem("installation instructions");
		mnHelp.add(mntmInstallationInstructions);
		
		mntmInstallationInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new InstallationInstructions();
			}
		});
		
		mntmFeedback = new JMenuItem("Feedback");
		mnHelp.add(mntmFeedback);
		
	
		mntmFeedback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Feedback.main(null);
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		
		/*mntmReport = new JMenuItem("Report a problem");
		mnHelp.add(mntmReport);
		
		mntmReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Report.main(null);
			f		
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		}); */
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		explorerFrame = new JInternalFrame("Project Explorer");
		explorerFrame.getContentPane().setBackground(new Color(139, 176, 244));
		explorerFrame.setVisible(true);

		slideFrame = new JInternalFrame("Recorder");
		slideFrame.getContentPane().setBackground(Color.WHITE);
		SpringLayout springLayout_1 = new SpringLayout();
		slideFrame.getContentPane().setLayout(springLayout_1);

		panel = new JPanel();
		springLayout_1.putConstraint(SpringLayout.NORTH, panel, -43, SpringLayout.SOUTH, slideFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, slideFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, slideFrame.getContentPane());
		panel.setBackground(new Color(220, 220, 220));
		slideFrame.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		btnRecord = new JButton("");
		sl_panel.putConstraint(SpringLayout.NORTH, btnRecord, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnRecord, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnRecord, -7, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnRecord, -608, SpringLayout.EAST, panel);
		btnRecord.setSelectedIcon(createImageIcon("resources/record.png"));
		//URL st = Workspace.class.getResource("");
		//System.out.println(st.getPath());
		//btnRecord.setSelectedIcon(new ImageIcon());
		btnRecord.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRecord.setToolTipText("Start Audio Recording");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Gif.main(null);
				if(playing &&(!paused))
				{
					paused=true;	
					btnRecord.setIcon(createImageIcon("resources/record.png"));
					btnRecord.setToolTipText("Resume Audio Recording");
					//playing=false;
				}
				else if(playing && paused)
				{
					System.out.println("playing and paused");
//					recFrame.showFrame();
					paused=false;
					btnRecord.setEnabled(true);
					btnRecord.setIcon(createImageIcon("resources/pause.png"));
					btnRecord.setToolTipText("Pause Audio Recording");
				}
				else 
				{
					btnRecord.setToolTipText("Pause Audio Recording");
					playing = true;
					if (currentSlide == null)
						setPreview(Integer.toString(currentSlideNumber));
					/* TODO pass selected segment */
					/* Place holder Segment *///currentSegment = new Segment(currentProject.getProjectURL());
					ProjectOperations.startAudioRecording(currentProject,
							currentSegment);
					WorkspaceUIHelper.disableRecord();
					
					System.out.println("outside thread,,,");
					
				}
			}
		});
		btnRecord.setIcon(createImageIcon("resources/record.png"));
		btnRecord.setBackground(new Color(176, 196, 222));
		panel.add(btnRecord);
		lblSlideDisplay = new JLabel("");
		springLayout_1.putConstraint(SpringLayout.NORTH, lblSlideDisplay, 0, SpringLayout.NORTH, slideFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.WEST, lblSlideDisplay, 0, SpringLayout.WEST, slideFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.SOUTH, lblSlideDisplay, -6, SpringLayout.NORTH, panel);
		springLayout_1.putConstraint(SpringLayout.EAST, lblSlideDisplay, 0, SpringLayout.EAST, slideFrame.getContentPane());
		springLayout_1.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, lblSlideDisplay);
		lblSlideDisplay.setHorizontalAlignment(SwingConstants.CENTER);

		stopbtn = new JButton("");
		sl_panel.putConstraint(SpringLayout.NORTH, stopbtn, 0, SpringLayout.NORTH, btnRecord);
		sl_panel.putConstraint(SpringLayout.SOUTH, stopbtn, -7, SpringLayout.SOUTH, panel);
		stopbtn.setSelectedIcon(createImageIcon("resources/stop.png"));
		stopbtn.setIcon(createImageIcon("resources/stop.png"));
		stopbtn.setToolTipText("Stop");
		stopbtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// hide RecordingFrame recFrame
//				try {
//					recFrame.hideFrame();
//				} catch (Exception f) {
//					f.printStackTrace();
//				}
				if(recFrame != null) {
					recFrame.hideFrame();
				}
				stopbtn.setEnabled(false);
				btnScreenRec.setToolTipText("Start Screen Recording");
				btnRecord.setToolTipText("Start Audio Recording");
				try {
					Thread.sleep(1600);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				recording.stopRecording();
				stopRecording();
				ProjectService.persist(currentProject);
			}
		});
		stopbtn.setFont(new Font("Tahoma", Font.PLAIN, 11));

		stopbtn.setBackground(new Color(176, 196, 222));
		panel.add(stopbtn);

		btnNext = new JButton("");
		sl_panel.putConstraint(SpringLayout.EAST, stopbtn, -333, SpringLayout.WEST, btnNext);
		sl_panel.putConstraint(SpringLayout.WEST, btnNext, -156, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNext, -111, SpringLayout.EAST, panel);
		btnNext.setIcon(createImageIcon("resources/navigate.png"));
		btnNext.setToolTipText("Go to Next Slide");
		btnNext.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "GoRight");
		btnNext.getActionMap().put("GoRight", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				changeSlideRight();
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * for(Slide libraryslide: librarySlides) {
				 * if(libraryslide.getName().equals(currentSlide.getName())) {
				 * int index=librarySlides.indexOf(libraryslide);
				 * if(index+1<librarySlides.size()) {
				 * System.out.println(librarySlides.get(index+1).getName());
				 * setPreview(librarySlides.get(index+1).getName());
				 * recording.stopSlide(); recording.startSlide(); } else {
				 * recording.stopSlide(); } break; }
				 * 
				 * }
				 */
				changeSlideRight();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnNext, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNext, -7, SpringLayout.SOUTH, panel);
		btnNext.setSelectedIcon(createImageIcon("resources/navigate.png"));
		btnNext.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNext.setBackground(new Color(176, 196, 222));
		panel.add(btnNext);

		
		btnDiscard = new JButton("");
		sl_panel.putConstraint(SpringLayout.NORTH, btnDiscard, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnDiscard, -7, SpringLayout.SOUTH, panel);
		btnDiscard.setToolTipText("Delete");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				discardRecording();
			}
		});
		sl_panel.putConstraint(SpringLayout.WEST, btnDiscard, 6, SpringLayout.EAST, btnNext);
		sl_panel.putConstraint(SpringLayout.EAST, btnDiscard, 51, SpringLayout.EAST, btnNext);
		btnDiscard.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDiscard.setEnabled(false);
		btnDiscard.setIcon(createImageIcon("resources/discard.png"));
		btnDiscard.setBackground(new Color(176, 196, 222));
		panel.add(btnDiscard);
		
		btnScreenRec = new JButton("");
		sl_panel.putConstraint(SpringLayout.WEST, stopbtn, 14, SpringLayout.EAST, btnScreenRec);
		sl_panel.putConstraint(SpringLayout.NORTH, btnScreenRec, 0, SpringLayout.NORTH, btnRecord);
		sl_panel.putConstraint(SpringLayout.WEST, btnScreenRec, 13, SpringLayout.EAST, btnRecord);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnScreenRec, -7, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnScreenRec, -548, SpringLayout.EAST, panel);
		btnScreenRec.setToolTipText("Start Screen Recording");
		btnScreenRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(screenRecordingFlag &&(!paused)) {
					paused=true;	
					btnScreenRec.setIcon(createImageIcon("resources/videocam.png"));
					btnScreenRec.setToolTipText("Resume Screen Recording");
					//playing=false;
				} else if(screenRecordingFlag && paused) {
					paused=false;
					btnScreenRec.setEnabled(true);
					btnScreenRec.setIcon(createImageIcon("resources/pause.png"));
					btnScreenRec.setToolTipText("Pause Screen Recording");
				} else {
					btnScreenRec.setToolTipText("Pause Screen Recording");
					if (currentSlide == null)
						setPreview(Integer.toString(currentSlideNumber));

					// disable button so that it can not be clicked during countdown
					btnScreenRec.setEnabled(false);
					JTimer.main(null);

					/* TODO pass selected segment */
					/* Place holder Segment *///currentSegment = new Segment(currentProject.getProjectURL());
				}
			}

			
		});
		
		btnScreenRec.setEnabled(false);
		btnScreenRec.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnScreenRec.setIcon(createImageIcon("resources/videocam.png"));
		btnScreenRec.setSelectedIcon(createImageIcon("resources/videocam.png"));
		btnScreenRec.setBackground(new Color(176, 196, 222));
		
		panel.add(btnScreenRec);
		
		timeDisplayLabel = new JLabel("--:--");
		sl_panel.putConstraint(SpringLayout.NORTH, timeDisplayLabel, 0, SpringLayout.NORTH, btnRecord);
		sl_panel.putConstraint(SpringLayout.WEST, timeDisplayLabel, 136, SpringLayout.EAST, stopbtn);
		timeDisplayLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(timeDisplayLabel);
		
		lblSlideDisplay.setIcon(createImageIcon("resources/start.jpg"));
		System.out.println(""+lblSlideDisplay.getWidth()+" "+lblSlideDisplay.getHeight());
		
		slideFrame.getContentPane().add(lblSlideDisplay);
		slideFrame.setVisible(true);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(10, 22, 267, 372);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		ImageIcon icon = createImageIcon("images/middle.gif");
		SpringLayout sl_contentPane = new SpringLayout();
		sl_contentPane.putConstraint(SpringLayout.NORTH, slideFrame, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, slideFrame, 6, SpringLayout.EAST, explorerFrame);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, slideFrame, 0, SpringLayout.SOUTH, explorerFrame);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, explorerFrame, 434, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, explorerFrame, 303, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, explorerFrame, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, explorerFrame, 0, SpringLayout.WEST, contentPane);
		contentPane.setLayout(sl_contentPane);
		explorerFrame.getContentPane().setLayout(null);

		presentationPanel = makePresPanel();
		tabbedPane.addTab("Presentation", icon, presentationPanel, "Edit Presentation in LibreOffice");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		// tabbedPane.addTab("Presentation", icon, panel2,"Does nothing");
		explorerFrame.getContentPane().add(tabbedPane);

		videosPanel = makeVideosPanel();
		tabbedPane.addTab("Videos", icon, videosPanel, "View videos");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);
		// tabbedPane.addTab("Presentation", icon, panel2,"Does nothing");
		explorerFrame.getContentPane().add(tabbedPane);

		outputPanel = makeOutputPanel();
		tabbedPane.addTab("Output", icon, outputPanel, "View output video");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_3);
		// tabbedPane.addTab("Presentation", icon, panel2,"Does nothing");
		explorerFrame.getContentPane().add(tabbedPane);

		contentPane.add(explorerFrame);

		contentPane.add(slideFrame);

		timelineFrame = new JInternalFrame("Video Outline");
		sl_contentPane.putConstraint(SpringLayout.WEST, timelineFrame, 4, SpringLayout.WEST, explorerFrame);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, timelineFrame, -5, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, timelineFrame, 6, SpringLayout.SOUTH, explorerFrame);
		sl_contentPane.putConstraint(SpringLayout.EAST, timelineFrame, -4, SpringLayout.EAST, contentPane);
		timelineFrame.setResizable(true);
		//timelineFrame.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		timelineFrame.getContentPane().setBackground(new Color(176, 224, 230));
		timelineFrame.setSize(900, 100);
		timelineScroll = new JScrollPane();
		timelineScroll.setBackground(SystemColor.control);
		timelineScroll.setHorizontalScrollBarPolicy(
				   JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		timelineInnerPanel = new JCustomPanel();
		
		// timelineInnerPanel.setBounds(12, 12, 1144, 159);
		timelineInnerPanel.setBackground(SystemColor.control);
		timelineScroll.setViewportView(timelineInnerPanel);
		timelineInnerPanel.setLayout(new GridLayout(1, 0, 10, 10));
		//timelineInnerPanel.setBounds(0, 0, 5000, 158);
		timelineScroll.setVisible(true);
		SpringLayout springLayout_2 = new SpringLayout();
		
			springLayout_2.putConstraint(SpringLayout.NORTH, timelineScroll, 12, SpringLayout.NORTH, timelineFrame.getContentPane());
			springLayout_2.putConstraint(SpringLayout.WEST, timelineScroll, 12, SpringLayout.WEST, timelineFrame.getContentPane());
			springLayout_2.putConstraint(SpringLayout.SOUTH, timelineScroll, -12, SpringLayout.SOUTH, timelineFrame.getContentPane());
		
		timelineFrame.getContentPane().setLayout(springLayout_2);
	
		timelineFrame.getContentPane().add(timelineScroll);

		StitchToolbarpanel = new JPanel();
		
			springLayout_2.putConstraint(SpringLayout.EAST, timelineScroll, -38, SpringLayout.WEST, StitchToolbarpanel);
			springLayout_2.putConstraint(SpringLayout.NORTH, StitchToolbarpanel, 12, SpringLayout.NORTH, timelineFrame.getContentPane());
			springLayout_2.putConstraint(SpringLayout.WEST, StitchToolbarpanel, -101, SpringLayout.EAST, StitchToolbarpanel);
			springLayout_2.putConstraint(SpringLayout.SOUTH, StitchToolbarpanel, -12, SpringLayout.SOUTH, timelineFrame.getContentPane());
			springLayout_2.putConstraint(SpringLayout.EAST, StitchToolbarpanel, -18, SpringLayout.EAST, timelineFrame.getContentPane());
		
		StitchToolbarpanel.setBackground(SystemColor.control);
		timelineFrame.getContentPane().add(StitchToolbarpanel);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressBarFlag = 1;
				dialog=new ProgressDialog();
				
			}
		});
		SpringLayout sl_StitchToolbarpanel = new SpringLayout();
		sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, btnRefresh, 5, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, btnRefresh, 2, SpringLayout.WEST, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, btnRefresh, 26, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, btnRefresh, 98, SpringLayout.WEST, StitchToolbarpanel);
		StitchToolbarpanel.setLayout(sl_StitchToolbarpanel);
		btnRefresh.setIcon(null);
		btnRefresh.setFont(new Font("Dialog", Font.BOLD, 10));
		btnRefresh.setBackground(new Color(245, 245, 245));
		StitchToolbarpanel.add(btnRefresh);
		btnInsert = new JButton("Insert");
		btnInsert.setIcon(null);
		
		btnInsert.setBounds(2, 110, 96, 19);
		btnInsert.setFont(new Font("Dialog", Font.BOLD, 10));
		btnInsert.setBackground(new Color(245, 245, 245));
		
      
		btnDelete = new JButton("Delete");
		sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, btnDelete, 74, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, btnDelete, 95, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, btnDelete, 2, SpringLayout.WEST, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, btnDelete, 98, SpringLayout.WEST, StitchToolbarpanel);
		btnDelete.setIcon(null);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressBarFlag = 4;
				dialog=new ProgressDialog();
			}
		});
		btnDelete.setBackground(new Color(245, 245, 245));
		btnDelete.setFont(new Font("Dialog", Font.BOLD, 10));
		StitchToolbarpanel.add(btnDelete);
		
		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); 
        progressBar.setIndeterminate(false);
	      
        innerPanel = new JPanel();
        sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, innerPanel, 143, SpringLayout.NORTH, StitchToolbarpanel);
        sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, innerPanel, 2, SpringLayout.WEST, StitchToolbarpanel);
        sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, innerPanel, 173, SpringLayout.NORTH, StitchToolbarpanel);
        sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, innerPanel, 98, SpringLayout.WEST, StitchToolbarpanel);
	
        innerPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.add(progressBar);
        innerPanel.setVisible(false);
        innerPanel.setOpaque(true);
        
        
        lblNewLabel1 = new JLabel("    Please wait...");
        innerPanel.add(lblNewLabel1, BorderLayout.SOUTH);
        StitchToolbarpanel.add(innerPanel);
        
		btnStitch = new JButton("Create");
		sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, btnStitch, 28, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, btnStitch, 2, SpringLayout.WEST, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, btnStitch, 49, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, btnStitch, 98, SpringLayout.WEST, StitchToolbarpanel);
		btnStitch.setIcon(null);
		btnStitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progressBarFlag = 2;
				dialog=new ProgressDialog();
				//recording.stitch();
				
			}
		});
		btnStitch.setFont(new Font("Dialog", Font.BOLD, 10));
		btnStitch.setBackground(new Color(245, 245, 245));
		StitchToolbarpanel.add(btnStitch);
		
		btnSaveOrder = new JButton("Save");
		btnSaveOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProjectService.persist(currentProject);
			}
		});
		btnSaveOrder.setIcon(null);
		btnSaveOrder.setBounds(2, 60, 96, 19);
		btnSaveOrder.setFont(new Font("Dialog", Font.BOLD, 10));
		btnSaveOrder.setBackground(new Color(245, 245, 245));
		//StitchToolbarpanel.add(btnSaveOrder);

		btnAdd = new JButton("Add");
		sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, btnAdd, 51, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, btnAdd, 2, SpringLayout.WEST, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, btnAdd, 72, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, btnAdd, 98, SpringLayout.WEST, StitchToolbarpanel);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progressBarFlag = 3;
				dialog=new ProgressDialog();
				
			}
		});
		btnAdd.setFont(new Font("Dialog", Font.BOLD, 10));
		btnAdd.setBackground(new Color(245, 245, 245));
		StitchToolbarpanel.add(btnAdd);
		
		btnSelectAll = new JButton("Select All");
		btnSelectAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i=0;i<customPanelList.size();i++)
				{
					if(!customPanelList.get(i).isHighlighted ){
						customPanelList.get(i).addHiglighted();
					}
					
					
				}
				
			}
		});
		btnSelectAll.setFont(new Font("Dialog", Font.BOLD, 10));
		btnSelectAll.setBackground(new Color(245, 245, 245));
		StitchToolbarpanel.add(btnSelectAll);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, btnSelectAll, 97, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, btnSelectAll, 118, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, btnSelectAll, 2, SpringLayout.WEST, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, btnSelectAll, 98, SpringLayout.WEST, StitchToolbarpanel);
		
		btnUnselectAll = new JButton("Unselect All");
		btnUnselectAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i=0;i<customPanelList.size();i++)
				{
					if(customPanelList.get(i).isHighlighted ){
						customPanelList.get(i).removeHighlighted();
					}
				}
				
			}
		});
		btnUnselectAll.setFont(new Font("Dialog", Font.BOLD, 10));
		btnUnselectAll.setBackground(new Color(245, 245, 245));
		StitchToolbarpanel.add(btnUnselectAll);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.NORTH, btnUnselectAll, 120, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.SOUTH, btnUnselectAll, 141, SpringLayout.NORTH, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.WEST, btnUnselectAll, 2, SpringLayout.WEST, StitchToolbarpanel);
		sl_StitchToolbarpanel.putConstraint(SpringLayout.EAST, btnUnselectAll, 98, SpringLayout.WEST, StitchToolbarpanel);
		int h=(int)t.getScreenSize().getHeight()-((int)(3*t.getScreenSize().getHeight()/5));
		contentPane.add(timelineFrame);
		// Speaker's Notes Frame
		notesFrame = new JInternalFrame("Speaker's Notes");
		sl_contentPane.putConstraint(SpringLayout.EAST, slideFrame, -6, SpringLayout.WEST, notesFrame);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, notesFrame, 0, SpringLayout.SOUTH, explorerFrame);
		sl_contentPane.putConstraint(SpringLayout.NORTH, notesFrame, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, notesFrame, 1004, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, notesFrame, 0, SpringLayout.EAST, contentPane);
		notesFrame.getContentPane().setBackground(new Color(176, 196, 222));
		contentPane.add(notesFrame);
		notesFrame.setVisible(true);
		timelineFrame.setVisible(true);
		
		final SpringLayout springLayout = new SpringLayout();
		notesFrame.getContentPane().setLayout(springLayout);
		pathDef=GeneralUtils.getDocumentsPath();
		// Jlabel for Upload Notes from 
		JLabel lblNewLabel_1 = new JLabel("Upload notes from:");																
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 20, SpringLayout.NORTH, notesFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_1, 10, SpringLayout.WEST, notesFrame.getContentPane());
		
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		notesFrame.getContentPane().add(lblNewLabel_1);
		// JTextField for path of upload
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, -2, SpringLayout.NORTH, notesFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 2, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel_1, -6, SpringLayout.WEST, textField);
		springLayout.putConstraint(SpringLayout.WEST, textField, 139, SpringLayout.WEST, notesFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.EAST, notesFrame.getContentPane());
		textField.setText(pathDef);
		notesFrame.getContentPane().add(textField);
		textField.setColumns(10);
		
		// Browse Button
		btnNewButton = new JButton("Browse..");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 6, SpringLayout.SOUTH, lblNewLabel_1);
	
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path=new DirectoryChooser(pathDef,"").selectedfile;
				//System.out.println(path);
				textField.setText(path);
			}
		});
		notesFrame.getContentPane().add(btnNewButton);
		//Upload Button
		btnNewButton_1 = new JButton("Extract");
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 10, SpringLayout.WEST, notesFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_1, 4, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_1, -10, SpringLayout.EAST, notesFrame.getContentPane());
		notesFrame.getContentPane().add(btnNewButton_1);
		notesArea = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, notesArea, 9, SpringLayout.SOUTH, btnNewButton);
		springLayout.putConstraint(SpringLayout.WEST, notesArea, 10, SpringLayout.WEST, notesFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, notesArea, -10, SpringLayout.SOUTH, notesFrame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, notesArea, -10, SpringLayout.EAST, notesFrame.getContentPane());
		notesArea.setVisible(true);
		notesFrame.getContentPane().add(notesArea);
		
		this.disable();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	GeneralUtils.stopOfficeInstance();
		    	for(Segment s:refreshList)
					if(s.getSlide()!=null){
						s.getSlide().refresh();
					}
				refreshList.clear();
				for(File f: deleteList)
				{
					f.delete();
				}
				GeneralUtils.cleanUp(currentProject);
		    }
		});
		WindowStateListener listener = new WindowAdapter() {
		      public void windowStateChanged(WindowEvent evt) {
		        int oldState = evt.getOldState();
		        int newState = evt.getNewState();

		        /*if ((oldState & Frame.ICONIFIED) == 0 && (newState & Frame.ICONIFIED) != 0) {
		          System.out.println("Frame was iconized");
		        } else if ((oldState & Frame.ICONIFIED) != 0 && (newState & Frame.ICONIFIED) == 0) {
		          System.out.println("Frame was deiconized");
		        }*/

		        if ((oldState & Frame.MAXIMIZED_BOTH) == 0 && (newState & Frame.MAXIMIZED_BOTH) != 0) {
		          System.out.println("Frame was maximized");
		          
		        } else if ((oldState & Frame.MAXIMIZED_BOTH) != 0 && (newState & Frame.MAXIMIZED_BOTH) == 0) {
		          System.out.println("Frame was minimized");
		          
		        }
		      }
		    };
		    this.addWindowStateListener(listener);
	}

	public class JCustomPanel extends JPanel implements MouseListener,Transferable,DropTargetListener,Autoscroll
	{
		
	    Insets autoscrollInsets = new Insets(0, 0, 0, 0);
	    private DropTarget target;
     JCustomPanel(){
    	 super();
    	 target = new DropTarget(this,this);
     }
     public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{new DataFlavor(CustomPanel.class, "JPanel")};
     }

     public boolean isDataFlavorSupported(DataFlavor flavor) {
         return true;
     }

     public Object getTransferData(DataFlavor flavor) {
         return this;
     }


     public void dragEnter(DragSourceDragEvent dsde) {}
     public void dragOver(DragSourceDragEvent dsde) {}
     public void dropActionchanged(DragSourceDragEvent dsde) {}
     public void dragExit(DragSourceEvent dse) {}

     //when the drag finishes, then repaint the DnDButton
     //so it doesn't look like it has still been pressed down
     public void dragDropEnd(DragSourceDropEvent dsde) {
         repaint();
     }

    
	// AutoScroll methods.
    public void autoscroll(Point location)
    {
        //System.out.println("mouse at " + location);
        int top = 0, left = 0, bottom = 0, right = 0;
        Dimension size = getSize();
        Rectangle rect = getVisibleRect();
        int bottomEdge = rect.y + rect.height;
        int rightEdge = rect.x + rect.width;
        if (location.y - rect.y <= autoscrollMargin && rect.y > 0) top = autoscrollMargin;
        if (location.x - rect.x <= autoscrollMargin && rect.x > 0) left = autoscrollMargin;
        if (bottomEdge - location.y <= autoscrollMargin && bottomEdge < size.height) bottom = autoscrollMargin;
        if (rightEdge - location.x <= autoscrollMargin && rightEdge < size.width) right = autoscrollMargin;
        rect.x += right - left;
        rect.y += bottom - top;
    //    System.out.println("AUTOSCROLL-1");
        scrollRectToVisible(rect);
    }
    public Insets getAutoscrollInsets()
    {
        Dimension size = getSize();
        Rectangle rect = getVisibleRect();
        autoscrollInsets.top = rect.y + autoscrollMargin;
        autoscrollInsets.left = rect.x + autoscrollMargin;
        autoscrollInsets.bottom = size.height - (rect.y + rect.height) + autoscrollMargin;
        autoscrollInsets.right = size.width - (rect.x + rect.width) + autoscrollMargin;
        return autoscrollInsets;
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

		
	}
	public JProgressBar progressBar;
	private JLabel lblNewLabel1;
	
	class ProgressDialog extends JPanel
	implements ActionListener, 
	PropertyChangeListener{
		
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		 public Task task;
		
		 
		 class Task extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				
				switch(progressBarFlag){
				case 1:{
					setEnabled(false);
					for(Segment s:refreshList)
						if(s.getSlide()!=null){
							s.getSlide().refresh();
							setProgress(80/refreshList.size());
							progressBar.setValue(80/refreshList.size());
							System.out.println("Test progress "+80/refreshList.size());
						}
					refreshList.clear();
					Call.workspace.removeTimeline();
					Call.workspace.populateTimeline();
					Call.workspace.removeExplorerSlides();
					Call.workspace.populateExplorerSlides();
					setProgress(100);
					progressBar.setValue(100);

					Thread.sleep(500);
					setEnabled(true);
				}
				break;
				case 2:{
					currentProject = ProjectService.getInstance(currentProject.getProjectJsonPath());
					setProgress(10);
					progressBar.setValue(10);
					if(currentProject != null){
						ProjectOperations.stitch(currentProject);
						progressBar.setValue(90);
						setProgress(90);
						showOutput();
						setProgress(100);

						progressBar.setValue(100);

						Thread.sleep(500);
					}
				}
				break;
				case 3:{
					btnAdd.setEnabled(false);
					startOperation();
					Segment s=new Segment(currentProject.getProjectURL());
					setProgress(10);
					progressBar.setValue(10);
					currentProject.addSegment(s);
					setProgress(70);
					progressBar.setValue(70);
					ProjectService.persist(currentProject);
					setProgress(80);
					progressBar.setValue(80);
					repopulateProject();
					setProgress(100);

					progressBar.setValue(100);

					Thread.sleep(500);
					endOperation();
					btnAdd.setEnabled(true);
				}
				break;
				case 4:{
					
					startOperation();
					removeTimeline();
					CustomPanel.highlightCount=0;
					setProgress(10);
					progressBar.setValue(10);
					ArrayList<CustomPanel> selected=new ArrayList<CustomPanel>();
					int progress=10;
					for(CustomPanel c: customPanelList)
					{
						if(c.isHighlighted)
						{
							c.isHighlighted=false;
							c.setBorder(c.blackBorder);
							currentProject.softDeleteSegment(c.getSegment());
							selected.add(c);
						}
						if(progress<=90)
							progress+=10;	
						setProgress(progress);
						progressBar.setValue(progress);
					}
					for(CustomPanel c: selected)
					{
						customPanelList.remove(c);
					}
					selected.clear();
					timelineFrame.revalidate();
					timelineFrame.repaint();
					Call.workspace.initCustomPanel();
		            Call.workspace.removeTimeline();
					Call.workspace.populateTimeline();
					Call.workspace.removeExplorerSlides();
					Call.workspace.populateExplorerSlides();
					setProgress(100);

					progressBar.setValue(100);

					Thread.sleep(500);
					endOperation();
				}
				break;
				}
				progressBarFlag = 0;
				setProgress(0);
			//	progressBar.setValue(0);
				innerPanel.setVisible(false);
				return null;
		 }
		 


		public void propertyChange(PropertyChangeEvent evt) {
			
			if ("progress" == evt.getPropertyName()) {
	            int progress = (Integer) evt.getNewValue();
	            progressBar.setIndeterminate(false);
	            progressBar.setValue(progress);
	            System.out.println(progress);
			}
			
		}


		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

		 }
		ProgressDialog() {

			innerPanel.setVisible(true);
			innerPanel.setVisible(true);
	        System.out.println("Progress dialog created");
	        task = new Task();
	        task.addPropertyChangeListener(this);
	        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	        task.execute();
		}
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
}

	@Override
	public void windowStateChanged(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub
//		if(Call.workspace.screenRecordingFlag)
//    		Call.workspace.recFrame.hideFrame();
		
	}
	@Override
	public void windowLostFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub
		//   if(Call.workspace.screenRecordingFlag)
        //   		Call.workspace.recFrame.showFrame();
		
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}