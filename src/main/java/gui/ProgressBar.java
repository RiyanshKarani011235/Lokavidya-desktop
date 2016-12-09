package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.iitb.lokavidya.core.utils.GeneralUtils;

public class ProgressBar {

	private JProgressBar progressBar;
	
	public void start()
	{
		progressBar = new JProgressBar(0);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		//frame.getContentPane().add(progressBar);
	}
	public void show()
	{
		progressBar.setVisible(true);
		//int delay = (GeneralUtils.getNumberofSlides(path))*250; //milliseconds
		  ActionListener taskPerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		          //...Perform a task...
		    	  int p=(int)progressBar.getPercentComplete();
		    	  if(p>=74)
		    	  {
		    		  ((Timer)evt.getSource()).stop();
		    	  }
		    	  else
	    		  {
		    		  p+=25;
		    		  progressBar.setValue(p);
		    		  //frame.getContentPane().revalidate();
		    		  //frame.getContentPane().repaint();
	    		  }
		    	  
		      }
		  };
		  
		//Timer t1=new Timer(delay, taskPerformer);
		//t1.start();
		//t1.setRepeats(true);
		
		//Do task
		progressBar.setValue(100);
		
	}
}
