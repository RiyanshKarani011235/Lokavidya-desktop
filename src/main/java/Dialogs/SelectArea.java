package Dialogs;

import gui.Call;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

/** Getting a Rectangle of interest on the screen.
Requires the MotivatedEndUser API - sold separately. */
public class SelectArea {

	
	Toolkit tk = Toolkit.getDefaultToolkit();
	
	
    Dimension d = tk.getScreenSize();
	public JFrame frame;
	public Cursor c;
	JPanel panel;
	JLabel lblNewLabel;
    Rectangle captureRect;
    BufferedImage screen,old;
    private JPanel panel_1;
    private JPanel panel_2;
    private JButton btnConfirm;
    private JButton btnCancel;
    private JLabel lblUseKeyboardTo;


	private int height;


	private int width;
    SelectArea() {
    	try{
    	
    	frame=new JFrame("Select Recording Area");
    	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
    	Robot robot = new Robot();
        final Dimension screenSize = Toolkit.getDefaultToolkit().
                getScreenSize();
        height=(int) screenSize.getHeight();
		height=height-scnMax.bottom-scnMax.top;
		width=(int) screenSize.getWidth();
		width=width-scnMax.left-scnMax.right;
        final BufferedImage screen1 = robot.createScreenCapture(
                new Rectangle(scnMax.left,0+scnMax.top,width,height));

    	
    	frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("resources/drag.png").getImage(),new Point(0,0),"custom cursor"));

    	
    	
    	
    		Insets insets=frame.getInsets();
    		System.out.println(insets.top);
        	Rectangle crop=new Rectangle(0,insets.top ,width ,(height-insets.top));
            screen=cropImage(screen1,crop);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
          
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

            	
                super.paintComponent(g);
                g.drawImage(screen, 0, 0, null);
                if(captureRect!=null){
                	g.setColor(Color.RED);
                	g.drawRect(captureRect.x,captureRect.y,captureRect.width,captureRect.height);
                	lblNewLabel.setText("X: "+Integer.toString(captureRect.x)
                	+"Y: "+Integer.toString(captureRect.y)
                	+"WIDTH: "+Integer.toString(captureRect.width)
                	+"HEIGHT: "+Integer.toString(captureRect.height));
                	
                	//System.out.println(captureRect.x+" "+captureRect.y+" "+captureRect.width+" "+captureRect.height);
                	//frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("drag.png").getImage(),new Point(frame.getWidth(),frame.getHeight()),"custom cursor"));
                	System.out.println("Called rectangle");
                }
                System.out.println("Called method");
            }
           
        };

        
        panel.addMouseMotionListener(new MouseMotionAdapter() {

            Point start = new Point();

            @Override
            public void mouseMoved(MouseEvent me) {
            	//panel.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("/resources/drag.png").getImage(),new Point(800,800),"custom cursor"));
            	
                start = me.getPoint();
                lblNewLabel.setText("");
                lblNewLabel.setText("X: "+Integer.toString(start.x)
                +"Y: "+Integer.toString(start.y)	
            	+"WIDTH: "+Integer.toString(0)
            	+"HEIGHT: "+Integer.toString(0));
            }

            @Override
            public void mouseDragged(MouseEvent me) {


                Point end = me.getPoint();
                if(end.x<start.x){
                	if(end.y<start.y){
                		captureRect = new Rectangle(end.x,end.y,(start.x-end.x), (start.y-end.y));
                        panel.repaint();
                	}
                	else{
                		captureRect = new Rectangle(end.x,start.y,(start.x-end.x), (end.y-start.y));
                        panel.repaint();
                	}
                }
                else{
                	if(end.y<start.y){
                		captureRect = new Rectangle(start.x,end.y,(end.x-start.x), (start.y-end.y));
                        panel.repaint();
                	}
                	else{
                		captureRect = new Rectangle(start.x,start.y,(end.x-start.x), (end.y-start.y));
                        panel.repaint();
                	}
                }
                
            }
        });
        


        
        
        
        panel.addMouseListener(new MouseListener() {
			

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				//set x,y,width,height
				
			}
			
		
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		
			public void mouseEntered(MouseEvent e) {
			
				// TODO Auto-generated method stub
				
			}


			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
	
			
		});
        
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setEnabled(true);
        panel.setVisible(true);
        panel.setOpaque(false);
        panel.setLayout(null);
        
        panel_1 = new JPanel();
       // panel_1.setBorder(BorderFactory.createLineBorder(Color.black));
       panel_1.setBackground(new Color(255, 218, 185));
        panel.add(panel_1);
        panel_1.setBounds(82, 11, 358, 33);
        lblNewLabel = new JLabel("X: 0");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setForeground(Color.RED);
        panel_1.add(lblNewLabel);
        lblNewLabel.setBackground(new Color(240,240,240,200));
         
         panel_2 = new JPanel();
         FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
         panel_2.setBackground(new Color(255, 218, 185));
         panel_2.setBounds(82, 49, 358, 33);
         panel.add(panel_2);
         
         btnConfirm = new JButton("Confirm");
         
         // confirm button not working (Bug 1)
         
//         btnConfirm.addActionListener(new ActionListener() {
//         	public void actionPerformed(ActionEvent e) {
//         		System.out.println("Setting reccoordinates");
//         		Call.workspace.setCoordinates(captureRect.x,captureRect.y ,captureRect.width ,captureRect.height );
//         		panel.repaint();
//				frame.dispose();
//				Call.workspace.setVisible(true);
//				Call.workspace.toFront();
//         	}
//         });
         
         ActionListener confirmPressedActionListener = new ActionListener() {
        	 @Override
        	 public void actionPerformed(ActionEvent e) {
        		 confirmPressed();
        	 }
         };
         
         btnConfirm.setBackground(new Color(255, 218, 185));
         btnConfirm.setForeground(new Color(255, 0, 0));
         
         btnConfirm.addKeyListener(new KeyListener() {
        	 @Override
        	 public void keyTyped(KeyEvent e) {
        		 if(e.getKeyChar() == KeyEvent.VK_ENTER) {
        			 confirmPressed();
        		 }
        	 }
        	 
        	 @Override
        	 public void keyPressed(KeyEvent e) {}
        	 
        	 @Override
        	 public void keyReleased(KeyEvent e) {}
        });
         
         panel_2.add(btnConfirm);
         
         btnCancel = new JButton("Cancel");
         btnCancel.setBackground(new Color(255, 218, 185));
         btnCancel.setForeground(new Color(255, 0, 0));
         
         // Cancel button not working (Bug 2)
         btnCancel.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
//         		Call.workspace.mntmDecideRecordingArea.setSelected(false);
//         		panel.repaint();
//				frame.dispose();
//				Call.workspace.setVisible(true);
//				Call.workspace.toFront();
         		cancelPressed();
         	}
         });
         
         btnCancel.addKeyListener(new KeyListener() {
        	 @Override
        	 public void keyTyped(KeyEvent e) {
        		 if(e.getKeyChar() == KeyEvent.VK_ENTER) {
        			 cancelPressed();
        		}
        	}
	    	 
        	@Override
	    	public void keyPressed(KeyEvent e) {}
	    	 
        	@Override
	    	public void keyReleased(KeyEvent e) {}
         });
         
         panel_2.add(btnCancel);
         
         lblUseKeyboardTo = new JLabel("Use keyboard to select");
         lblUseKeyboardTo.setForeground(Color.RED);
         lblUseKeyboardTo.setFont(new Font("Tahoma", Font.BOLD, 11));
         lblUseKeyboardTo.setBackground(new Color(240, 240, 240, 200));
         panel_2.add(lblUseKeyboardTo);
        
        frame.setUndecorated(true);
        frame.setBackground(new Color(255, 0, 0, 7));
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        
        
        //RecordingFrame captureRect = new RecordingFrame(0,0,800,600);
        //System.out.println("Rectangle of interest: " + captureRect);
    }

 
    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }
    
    public static void main(String[] args) throws Exception {
        

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SelectArea();
            }
        });
    }
    
    // (Bug 1)
    public void confirmPressed() {
    	System.out.println("setting recoordinated");
	   	if(captureRect != null) {
	   		Call.workspace.setCoordinates(captureRect.x,captureRect.y ,captureRect.width ,captureRect.height );
	   		panel.repaint();
	   		frame.dispose();
	   		Call.workspace.setVisible(true);
	   		Call.workspace.toFront();
	   	} else {
	   		System.out.println("captureRect is null");
	   		JOptionPane.showMessageDialog(null, "capture area can not be empty", "Error", JOptionPane.INFORMATION_MESSAGE);
	   	}
    }
   	
    // (Bug 2)
   	public void cancelPressed() {
   		Call.workspace.mntmDecideRecordingArea.setSelected(false);
   		panel.repaint();
   		frame.dispose();
   		Call.workspace.setVisible(true);
   		Call.workspace.toFront();
	}
   	
}
