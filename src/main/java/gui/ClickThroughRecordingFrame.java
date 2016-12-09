package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by ironstein on 01/09/16.
 */
public class ClickThroughRecordingFrame {

    private int lineWidth = 3;
    private boolean toDisplay = false;

    private Line north;
    private Line south;
    private Line west;
    private Line east;

    public ClickThroughRecordingFrame(int x, int y, int width, int height) {

        north = new Line("horizontal", width + (2*lineWidth), x-lineWidth, y-lineWidth);
        south = new Line("horizontal", width, x, y+height);
        west = new Line("vertical", height + (2*lineWidth), x-lineWidth, y-lineWidth);
        east = new Line("vertical", height+lineWidth, x+width, y);
    }
 
    public void show() {
        north.setVisible(true);
        south.setVisible(true);
        west.setVisible(true);
        east.setVisible(true);
        toDisplay = true;
    }

    public void hide() {
        north.setVisible(false);
        south.setVisible(false);
        west.setVisible(false);
        east.setVisible(false);
        toDisplay = false;
    }
    
    // for backwards compatibility (implementation with a Window instead of Line)
    public void setVisible(boolean visibility) {
    	if (visibility == true) {
    		this.show();
    	} else {
    		this.hide();
    	}
    }

    public void dispose() {
        north.dispose();
        south.dispose();
        west.dispose();
        east.dispose();
    }
    
    public void toFront() {
    	north.toFront();
    	south.toFront();
    	west.toFront();
    	east.toFront();
    }

    public class Line extends JDialog {

        public Line(String type, int dimension) {
            createLine(type, dimension);
        }

        public Line(String type, int dimension, int x, int y) {
            createLine(type, dimension);
            this.setLocation(x, y);
        }

        public void createLine(String type, int dimension) {
            this.setUndecorated(true);
            this.setAlwaysOnTop(true);
            this.getContentPane().setBackground(Color.RED);

            // creates a problem where transparency is not supported
//            this.setOpacity(new Float(0.5));

            if (type == "horizontal") {
                // draw a horizontal JFrame
                this.setSize(dimension, lineWidth);
            } else if (type == "vertical") {
                // draw a vxertical JFrame
                this.setSize(lineWidth, dimension);
            }
            
            // set invisible by default
            this.setVisible(false);
        }

        @Override
        public void toFront() {
        	System.out.println("toFront called");
        	System.out.println("toDiaplay : " + toDisplay);
//            if(!this.isShowing() && toDisplay) {
//                int sta = super.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;
//
//                super.setExtendedState(sta);
                super.setAlwaysOnTop(true);
                super.toFront();
                super.requestFocus();
                super.setAlwaysOnTop(false);
//            }
        }
//
//        protected void processWindowEvent(WindowEvent e) {
//            System.out.println("ClickThroughRecordingFrame : processWindowEvent : " + e.toString());
//            if(e.getID() == WindowEvent.WINDOW_DEACTIVATED) {
//            	System.out.println("window deactivated");
//                java.awt.EventQueue.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
////                        line.toFront();
////                        line.repaint();)
//                        toFront();
//                    }
//                });
//            }
//        }
    }

//    public static void main(String[] args) {
//        ClickThroughRecordingFrame clickThroughRecordingFrame = new ClickThroughRecordingFrame(100, 100, 500, 500);
//        clickThroughRecordingFrame.show();
//        try {
//            Thread.sleep(5000);
//        } catch (java.lang.InterruptedException e) {
//            // pass
//        }
//        clickThroughRecordingFrame.hide();
//    }
}
