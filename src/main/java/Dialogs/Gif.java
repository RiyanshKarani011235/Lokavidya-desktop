package Dialogs;

import gui.Call;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Gif extends JFrame {
    JPanel contentPane;
    JLabel imageLabel = new JLabel();
    JLabel headerLabel = new JLabel();

    public Gif() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            contentPane = (JPanel) getContentPane();
            contentPane.setLayout(new BorderLayout());
            setSize(new Dimension(400, 300));
            setTitle("Screen Recording");
            // add the header label
            headerLabel.setFont(new java.awt.Font("Comic Sans MS", Font.BOLD, 16));
            headerLabel.setText("Starting your recording");
            contentPane.add(headerLabel, java.awt.BorderLayout.NORTH);
            // add the image label
            ImageIcon ii = new ImageIcon("images\\loading2.gif");
            //Image scaleImage = ii.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT);
            imageLabel.setIcon(new ImageIcon(Gif.class.getResource("/resources/loading.gif")));
            contentPane.add(imageLabel, java.awt.BorderLayout.CENTER);
            // show it
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Gif v=new Gif();
        
        Timer t = new Timer(1000 * 5, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // do your reoccuring task
            	v.dispose();
            	//Call.workspace.startRecord();
            }
        });
        t.start();
    }

}