package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import com.iitb.lokavidya.core.data.Segment;

import gui.Workspace.JCustomPanel;

public class CustomPanel extends JPanel implements MouseListener,Transferable, DragSourceListener, DragGestureListener,DropTargetListener,Autoscroll 
{
    boolean isHighlighted;
    public static int highlightCount;
    public static  int slideCount;
    public int id;
    
    Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    Border blueBorder = BorderFactory.createLineBorder(Color.BLUE,5);
    Border redBorder = BorderFactory.createLineBorder(Color.RED,5);
    public Segment segment;
    public JCheckBox checkBox;
    public SpringLayout springLayout;
    private DragSource source;
    private TransferHandler t;
    private DropTarget target;
    static int autoscrollMargin = 73;
    Insets autoscrollInsets = new Insets(0, 0, 0, 0);
    public void init() {
    	
    }
    
    
    CustomPanel(final Segment segment)
    {
    	System.out.println(slideCount);
    	this.setBackground(new Color(245,245,245));
    	springLayout = new SpringLayout();
    	setLayout(springLayout);
    	this.setSize(207, 152);
    	this.setPreferredSize(new Dimension(207, 147));
    	//this.setMaximumSize();
    	id = slideCount;
    	++slideCount;
    	addMouseListener(this);
        setBorder(blackBorder);
        setFocusable(true);
        checkBox = new JCheckBox();
        this.springLayout.putConstraint(SpringLayout.NORTH,checkBox, 3, SpringLayout.NORTH, this);
        this.springLayout.putConstraint(SpringLayout.WEST, checkBox, 180, SpringLayout.WEST, this);
        this.add(checkBox);

        checkBox.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				checkboxClicked();
			}
		});
        /*
         *  Drop Section
         */
        
        target = new DropTarget(this,this);

        /*
         *  Drag Section
         */
        t = new TransferHandler(){

            public Transferable createTransferable(JPanel c){
                  return new CustomPanel(segment);
            }
        	
        };
        setTransferHandler(t);
        
        source = new DragSource();
        source.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
      
        this.segment = segment;
    }
//
//    @Override
//    public Dimension getPreferredSize()
//    {
//        return new Dimension(200, 100);
//    }
    public void setPreview()
    {
    	setBorder(redBorder);
    }
    public void stopPreview()
    {
    	setBorder(blackBorder);
    }
    public void checkboxClicked()
    {
        if(checkBox.isSelected())
        {
        	setBorder(blueBorder);
        	++highlightCount;
            isHighlighted=!isHighlighted;
            
        	
        }
        else{
        	setBorder(blackBorder);
        	if (highlightCount > 0){
        		isHighlighted=!isHighlighted;
        		--highlightCount;
        	Call.workspace.highlightCurrent();	
        }
    }
        
   }
    
 public void addHiglighted(){
	 setBorder(blueBorder);
 	
 	++highlightCount;
     isHighlighted=!isHighlighted;
       checkBox.setSelected(true);
 }
 public void removeHighlighted(){
	 setBorder(blackBorder);
 	if (highlightCount > 0){
 		isHighlighted=!isHighlighted;
 		checkBox.setSelected(false);
 		
 		--highlightCount;
 	}
 }
 public void mouseClicked(MouseEvent e)
    {
        if(isHighlighted)
        {
        	this.removeHighlighted();
        }
        else if (!isHighlighted) {
        	this.addHiglighted();
        }
        
    }

    public Segment getSegment() {
    	return this.segment;
    }
    

     public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
     public void mouseEntered(MouseEvent e){
    	    System.out.println("Mouse to " + e.getPoint());
     }
    public void mouseExited(MouseEvent e){}
    public void mouseMoved(MouseEvent e) {


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

    //when a DragGesture is recognized, initiate the Drag
    public void dragGestureRecognized(DragGestureEvent dge) {
    	
    	System.out.println("Recognised!!! "+this.id);
    	Call.workspace.setinitialDragID(this.id%(Call.workspace.currentProject.getOrderedSegmentList().size()));
         source.startDrag(dge, DragSource.DefaultMoveDrop, new CustomPanel(segment), this);        
    }


    public void drop(DropTargetDropEvent dtde) {
    //	System.out.println("Entered-1!!");
       
              
             //get Transfer data
            Transferable t = dtde.getTransferable();

             //get the Data flavors transferred with the Transferable
            DataFlavor[] d = t.getTransferDataFlavors();

            
            System.out.println(this.id);
            //and if the DataFlavors match for the DnDTable 
            //(ie., we don't want an ImageFlavor marking an image transfer)
           System.out.println(CustomPanel.this.id);
            Call.workspace.startOperation();
            Call.workspace.currentProject.insertAt(CustomPanel.this.id%(Call.workspace.currentProject.getOrderedSegmentList().size()),Call.workspace.getinitialDragID()); 
            Call.workspace.initCustomPanel();
            Call.workspace.removeTimeline();
			Call.workspace.populateTimeline();
			Call.workspace.removeExplorerSlides();
			Call.workspace.populateExplorerSlides();
			Call.workspace.endOperation();
           
    }
    
    public static class PanelDataFlavor extends DataFlavor {

        // This saves me having to make lots of copies of the same thing
        public static PanelDataFlavor SHARED_INSTANCE = new PanelDataFlavor();

        public PanelDataFlavor() {

            super(JPanel.class, null);

        }

    }
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dragOver(DropTargetDragEvent dtde) {
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
	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
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
        scrollRectToVisible(rect);
       // System.out.println("AUTOSCROLL");
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

    
}

