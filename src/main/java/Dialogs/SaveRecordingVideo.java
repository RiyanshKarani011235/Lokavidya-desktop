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
	
	public void run() {
		
	}
	
}
