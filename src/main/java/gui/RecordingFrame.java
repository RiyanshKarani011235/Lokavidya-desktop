/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Joey
 */
public class RecordingFrame {

    public static ClickThroughRecordingFrame w;
//    public static Window w;

    public RecordingFrame(final int startx,final int starty,final int recwidth,final int recheight) {

//       	w = new Window(null) {
//       		@Override
//			public void paint(Graphics g) {
//		    	final Font font = getFont().deriveFont(48f);
//		    	g.setFont(font);
//				g.setColor(Color.RED);
//
//		    	g.drawRect(startx,starty,recwidth,recheight);
//		  	}
//            @Override
//            public void update(Graphics g)
//            {
//                paint(g);
//            }
//        };
//
//        w.setAlwaysOnTop(true);
//        w.setBounds(w.getGraphicsConfiguration().getBounds());
//        w.setBackground(new Color(0,0,0,0));
//        w.setOpacity(0);

//        Window w=new Window(null)
//        {
//            Shape shape;
//            @Override
//            public void paint(Graphics g)
//            {
//                Graphics2D g2d = ((Graphics2D)g);
//                if(shape==null)
//                {
//                    Font f=getFont().deriveFont(48f);
//                    FontMetrics metrics = g.getFontMetrics(f);
//                    final String message = "Hello";
//                    shape=f.createGlyphVector(g2d.getFontRenderContext(), message)
//                            .getOutline(
//                                    (getWidth()-metrics.stringWidth(message))/2,
//                                    (getHeight()-metrics.getHeight())/2);
//                    com.sun.awt.AWTUtilities.setWindowShape(this, shape);
//                    setShape(shape);
//                }
//                g.setColor(Color.RED);
//                g2d.fill(shape.getBounds());
//            }
//            @Override
//            public void update(Graphics g)
//            {
//                paint(g);
//            }
//        };
//        w.setAlwaysOnTop(true);
//        w.setBounds(w.getGraphicsConfiguration().getBounds());
//        w.setVisible(true);

        w = new ClickThroughRecordingFrame(startx, starty, recwidth, recheight);
        // just for the sake of backward compatibility
  }

    public void showFrame(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                w.setVisible(true);
            }
        });
    }
    public void hideFrame(){
        w.setVisible(false);
    }
    
    public void toFront() {
    	w.toFront();
    }

//    public static void main(String args[])
//    {
//    	RecordingFrame r = new RecordingFrame(20,20,100,100);
//    	r.showFrame();
//    	//r.decideFrame();
//    }
}