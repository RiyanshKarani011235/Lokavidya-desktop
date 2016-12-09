package gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class Trial extends JPanel {

	/**
	 * Create the panel.
	 */
	public Trial() {

		this.setBackground(new Color(245,245,245));
    	this.setSize(207, 152);
    	SpringLayout springLayout = new SpringLayout();
    	setLayout(springLayout);
    	JLabel l1=new JLabel("Slide 1");
    	springLayout.putConstraint(SpringLayout.NORTH, l1, 7, SpringLayout.NORTH, this);
    	springLayout.putConstraint(SpringLayout.WEST, l1, 7, SpringLayout.WEST, this);
    	springLayout.putConstraint(SpringLayout.SOUTH, l1, 31, SpringLayout.NORTH, this);
    	springLayout.putConstraint(SpringLayout.EAST, l1, 133, SpringLayout.WEST, this);
    	this.add(l1);
    	
    	JLabel lblNewLabel = new JLabel("");
    	lblNewLabel.setIcon(new ImageIcon("C:\\Users\\hp\\Pictures\\thumbnail.png"));
    	springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 6, SpringLayout.SOUTH, l1);
    	springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, l1);
    	add(lblNewLabel);
    	
    	JButton btnEditSlide = new JButton("Edit Slide");
    	springLayout.putConstraint(SpringLayout.WEST, btnEditSlide, 0, SpringLayout.WEST, l1);
    	springLayout.putConstraint(SpringLayout.SOUTH, btnEditSlide, 0, SpringLayout.SOUTH, this);
    	add(btnEditSlide);
    	
    	JLabel lblNewLabel_1 = new JLabel("Has Audio");
    	lblNewLabel_1.setIcon(new ImageIcon("F:\\IITB\\lokavidya-desktop\\resources\\mic.png"));
    	springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 0, SpringLayout.NORTH, lblNewLabel);
    	springLayout.putConstraint(SpringLayout.EAST, lblNewLabel_1, -10, SpringLayout.EAST, this);
    	add(lblNewLabel_1);
    	
    	JButton btnNewButton = new JButton("Play");
    	springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 10, SpringLayout.SOUTH, lblNewLabel_1);
    	springLayout.putConstraint(SpringLayout.WEST, btnNewButton, -63, SpringLayout.EAST, lblNewLabel_1);
    	springLayout.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, lblNewLabel_1);
    	add(btnNewButton);
    	
    	JButton btnDelete = new JButton("Delete");
    	springLayout.putConstraint(SpringLayout.NORTH, btnDelete, 6, SpringLayout.SOUTH, btnNewButton);
    	springLayout.putConstraint(SpringLayout.WEST, btnDelete, 17, SpringLayout.EAST, lblNewLabel);
    	springLayout.putConstraint(SpringLayout.EAST, btnDelete, 0, SpringLayout.EAST, lblNewLabel_1);
    	add(btnDelete);
	}
}
