/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Joey
 */
public class LibSlide {
    private String name,type;
    private Icon icon;
    private int order;
    private int timestamp;
    private String file;
    private BufferedImage image;
    public String getName(){
        return name;
    }
     public String getFile(){
        return file;
    }
    public String getType(){
        return type;
    }
    public Icon getIcon(){
        return icon;
    }
    public int getOrder(){
        return order;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setType(String type){
        this.type=type;
    }
     public void setFile(String file){
        this.file=file;
    }
    public void  setIcon(Icon icon){
        this.icon=icon;
    }
    public void setOrder(int order){
        this.order=order;
    }
     public int getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(int timestamp){
        this.timestamp=timestamp;
    }
    public ImageIcon getThumbnail(){
      ImageIcon img=(ImageIcon)icon;
      int height=icon.getIconHeight(),width=icon.getIconWidth();
      while(width>125){
    	  height=height*(110)/width;
    	  width=110;
      }
      Image scaleImage = img.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT);
     return new ImageIcon(scaleImage);
    }
    public ImageIcon getSmallImage(){
        ImageIcon img=(ImageIcon)icon;
        int height=icon.getIconHeight(),width=icon.getIconWidth();
        while(width>125){
      	  height=height*(70)/width;
      	  width=70;
        }
        Image scaleImage = img.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT);
       return new ImageIcon(scaleImage);
      }
    public ImageIcon getPreview(){
      ImageIcon img=(ImageIcon)icon;
      int height=icon.getIconHeight(),width=icon.getIconWidth();
      while(width>586){
          height=height*(586)/width;
          width=586;
      }
      Image scaleImage = img.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT);
     return new ImageIcon(scaleImage);
    }
    public ImageIcon getFrame(){
      ImageIcon img=(ImageIcon)icon;
      
      Image scaleImage = img.getImage().getScaledInstance(800, 600,Image.SCALE_DEFAULT);
     return new ImageIcon(scaleImage);
    }
    public BufferedImage getBufferedImage(){
        if(image==null) createBufferedImage();
            return image;
        
    }
    public void createBufferedImage(){
      File img = new File(this.file);
        System.out.println(this.file);
           image = 
              new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
          

            try {  
                image = ImageIO.read(img );
                 image=resize(image,800, 600); 
            }  
            catch (Exception e) {
            	e.printStackTrace();
            }
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
} 
}
