package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class WorkspaceUIHelper {

	public static void disableAllRecordingToolkit()
	{
		Call.workspace.btnRecord.setEnabled(false);
		Call.workspace.btnScreenRec.setEnabled(false);
		Call.workspace.btnDiscard.setEnabled(false);
		Call.workspace.btnNext.setEnabled(false);
		Call.workspace.stopbtn.setEnabled(false);
		Timer t = new Timer(1000 * 4, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // do your reoccuring task
            	
            	try {
            		Call.workspace.btnRecord.setEnabled(true);
            		Call.workspace.btnDiscard.setEnabled(true);
            		Call.workspace.btnNext.setEnabled(true);
            		Call.workspace.stopbtn.setEnabled(true);
            		
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        t.start(); 
        t.setRepeats(false);
	}

	public static void disableRecord()
	{
		Call.workspace.btnRecord.setEnabled(false);
		Call.workspace.btnScreenRec.setEnabled(false);
		Call.workspace.btnDiscard.setEnabled(false);
		Call.workspace.btnNext.setEnabled(false);
		Call.workspace.stopbtn.setEnabled(false);
		Call.workspace.btnAdd.setEnabled(false);
		Call.workspace.btnDelete.setEnabled(false);
		Call.workspace.btnSelectAll.setEnabled(false);
		Call.workspace.btnUnselectAll.setEnabled(false);
		Call.workspace.btnRefresh.setEnabled(false);
		if(Call.workspace.playing)
		{
			Call.workspace.btnRecord.setEnabled(true);
			Call.workspace.btnRecord.setIcon(Call.workspace.createImageIcon("resources/pause.png"));
		}
		else if (Call.workspace.screenRecordingFlag)
		{
			Call.workspace.btnScreenRec.setEnabled(true);
			Call.workspace.btnScreenRec.setIcon(Call.workspace.createImageIcon("resources/pause.png"));
		}
		Call.workspace.stopbtn.setEnabled(true);
		Call.workspace.btnNext.setEnabled(true);
		Call.workspace.btnDiscard.setEnabled(true);
		disableTimeline();
		/*Timer t = new Timer(1000 * 4, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // do your reoccuring task
            	
            	try {
            		Call.workspace.stopbtn.setEnabled(true);
            		Call.workspace.btnNext.setEnabled(true);
            		Call.workspace.btnDiscard.setEnabled(true);
            		disableTimeline();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        }); */
      //  t.start(); 
      //  t.setRepeats(false);
	}
	public static void disableTimeline()
	{
		Call.workspace.btnStitch.setEnabled(false);

		Call.workspace.btnSaveOrder.setEnabled(false);
		Call.workspace.btnDelete.setEnabled(false);
	}
	public static void enableTimeline()
	{
		Call.workspace.btnStitch.setEnabled(true);

		//Call.workspace.btnSaveOrder.setEnabled(true);
		Call.workspace.btnDelete.setEnabled(true);
		Call.workspace.btnAdd.setEnabled(true);
		Call.workspace.btnDelete.setEnabled(true);
		Call.workspace.btnSelectAll.setEnabled(true);
		Call.workspace.btnUnselectAll.setEnabled(true);
		Call.workspace.btnRefresh.setEnabled(true);
	}
	public static void disableStop()
	{
		Call.workspace.btnRecord.setEnabled(false);
		Call.workspace.btnScreenRec.setEnabled(false);
		Call.workspace.btnDiscard.setEnabled(false);
		Call.workspace.btnNext.setEnabled(false);
		Call.workspace.stopbtn.setEnabled(false);
		/*Timer t = new Timer(1000 * 4, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // do your reoccuring task
            	
            	try {
            		Call.workspace.btnScreenRec.setIcon(Call.workspace.createImageIcon("resources/videocam.png"));
            		Call.workspace.btnRecord.setIcon(Call.workspace.createImageIcon("resources/record.png"));
            		
            		Call.workspace.stopbtn.setEnabled(false);
            		Call.workspace.btnNext.setEnabled(false);
            		Call.workspace.btnDiscard.setEnabled(false);
            		Call.workspace.btnRecord.setEnabled(true);
            		Call.workspace.btnScreenRec.setEnabled(true);
            		
            		enableTimeline();
            		
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        t.start(); 
        t.setRepeats(false); */
		try {
    		Call.workspace.btnScreenRec.setIcon(Call.workspace.createImageIcon("resources/videocam.png"));
    		Call.workspace.btnRecord.setIcon(Call.workspace.createImageIcon("resources/record.png"));
    		
    		Call.workspace.stopbtn.setEnabled(false);
    		Call.workspace.btnNext.setEnabled(false);
    		Call.workspace.btnDiscard.setEnabled(false);
    		Call.workspace.btnRecord.setEnabled(true);
    		Call.workspace.btnScreenRec.setEnabled(true);
    		
    		enableTimeline();
    		
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
