package com.iitb.lokavidya.core.utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.commons.lang.*;

public class Ppt {
	public String path,location;
	public String wrap(String v,int factor)
	{
		StringBuilder sb = new StringBuilder(v);

		int i = 0,j=0,oldj=0;
		while((i<sb.length()))
		{
			j=i;
			while((j<i+factor)&&(j<sb.length())&&(j!=-1))
			{
				oldj=j;
				j=sb.indexOf(" ", j+1);
			}
			if(j!=-1)
			{
				if(i!=oldj)
				{i=oldj;}
				else{i=j;}
				sb.replace(i, i + 1, "\n");
			}
			else if((j==-1)&&(i!=oldj))
			{
				i=oldj;
				System.out.println("New i is "+i);
				sb.replace(i, i + 1, "\n");
				break;
			}
			else
			{
				break;
			}
				
		}
		System.out.println(sb);
		return sb.toString();
	}
	public void NoviceCheck()
	{
		
	}
 	public void getImages()
	{
		try
		{
			File file=new File(path);
		      XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
		      
		      //getting the dimensions and size of the slide 
		      Dimension pgsize = ppt.getPageSize();
		      List<XSLFSlide> slide = ppt.getSlides();
		      
		      for (int i = 0; i < slide.size(); i++) {
		         BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.TYPE_INT_RGB);
		         Graphics2D graphics = img.createGraphics();
	
		         //clear the drawing area
		         graphics.setPaint(Color.white);
		         graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
	
		         //render
		         slide.get(i).draw(graphics);
		         
		       //creating an image file as output
		         //String projectPath=new File(path).getParentFile().getAbsolutePath();
		         String fileName="ppt_image"+Integer.toString(i)+".png";
		         fileName=new File(location,fileName).getAbsolutePath();
		         FileOutputStream out = new FileOutputStream(fileName);
		         javax.imageio.ImageIO.write(img, "png", out);
		         ppt.write(out);
		         out.close();
		      }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getNotes(int factor,int index)
	{
		String[] newstr= new String[20];
		//String path="F:\\IITB\\Ocean currents.pptx";
		int i=0;
		try
		{
			FileInputStream fis = new FileInputStream(path);
		    XMLSlideShow pptxshow = new XMLSlideShow(fis);
		    
		    for(XSLFSlide slide : pptxshow.getSlides()) 
		    { 
	            XSLFNotes mynotes = slide.getNotes(); 
	            List<XSLFShape> shapeList=Collections.emptyList();
	            try{
	            shapeList=mynotes.getShapes();}
	            catch(NullPointerException e)
	            {
	            	//shapeList=null;
	            }
	            if(shapeList.isEmpty()!=true)
	            {
	            	
		            for (XSLFShape shape : shapeList) 
		            {
		            	if (shape instanceof XSLFTextShape) {
		            		XSLFTextShape txShape = (XSLFTextShape) shape;
		            		for (XSLFTextParagraph xslfParagraph : txShape.getTextParagraphs()) {
		            			String str = xslfParagraph.getText();
		            			if(str.length()>4)
		            			{
		            				newstr[i]=wrap(str,factor);
		            				//System.out.println(newstr[i]);
		            				i++;
		            			}
	
	                    	}
		            	}	
		            }
	            }    
		    }    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try{
		if(newstr[index-1].equals(""))
		{
			newstr[index-1]="No notes provided";
		}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return newstr[index-1];
	}
	public String getBullets()
	{
		String text,returntext="";
		int bulletno=0,slideno=1;
		try
		{
	        FileInputStream is = new FileInputStream(path);
	        XMLSlideShow ppt = new XMLSlideShow(is);
	        
	        for(XSLFSlide slide : ppt.getSlides()) {
	        	bulletno=0;
	        	List<XSLFShape> shapeList=Collections.emptyList();
	        	shapeList=slide.getShapes();
	            for(XSLFShape shape : shapeList){
	                if(shape instanceof XSLFTextShape) {
	                    XSLFTextShape txShape = (XSLFTextShape)shape;
	                    //text=txShape.getText();
	                    //out.println(txShape.getText());
	                   for( XSLFTextParagraph para :txShape.getTextParagraphs())
	                   {
	                	   if(para.isBullet())
	                	   {
	                		   
	                		   text=para.getText();
	                		   String ch=para.getBulletCharacter();
	                		   bulletno+=1;
	                		   //System.out.println("Found para with "+bulletno+" number of bullets"+ch);
	                	   }   
	                   }
	                }
	                if(bulletno>7)
	                {
	                	returntext+="Slide "+ Integer.toString(slideno)+" has more than seven bullets\n";
	                }
	            }
	            slideno++;
	        }
	        is.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(returntext);
		return returntext;
	}
	public void adjustFont(String fontname,int Heading,int Text)
	{
		int slideno=1,parano=1;
		try
		{
			FileInputStream is = new FileInputStream(path);
			//FileInputStream is = new FileInputStream("F:/IITB/Ocean currents.pptx");
	        XMLSlideShow ppt = new XMLSlideShow(is);
	        
	        for(XSLFSlide slide : ppt.getSlides()) {
	        	parano=1;
	        	List<XSLFShape> shapeList=Collections.emptyList();
	        	shapeList=slide.getShapes();
	            for(XSLFShape shape : shapeList){
	                if(shape instanceof XSLFTextShape) {
	                    XSLFTextShape txShape = (XSLFTextShape)shape;
	                    //text=txShape.getText();
	                    //out.println(txShape.getText());
	                   for( XSLFTextParagraph para :txShape.getTextParagraphs())
	                   {
	                	   if(parano==1)
	                	   {
	                		   System.out.println(para.getText());
	                		   for(XSLFTextRun run: para.getTextRuns())
	                		   {
	                			   Double heading=(double) Heading;
	                			   run.setFontSize(heading);
	                			   run.setFontFamily(fontname);
	                		   }
	                		   parano++;
	                	   }
	                	   else
	                	   {
	                		   for(XSLFTextRun run: para.getTextRuns())
	                		   {
	                			   Double text=(double) Text;
	                			   run.setFontSize(text);
	                			   run.setFontFamily(fontname);
	                		   }
	                	   }
	                   }
	                }
	            }
	        }
	        //is.close();
	        FileOutputStream out = new FileOutputStream(path);
	        ppt.write(out);
	        out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public String checkmustHeadings(List<String> must)
	{
		int slideno=1,parano=1;
		String returntext="";
		try
		{
			FileInputStream is = new FileInputStream(path);
			//FileInputStream is = new FileInputStream("F:/IITB/Ocean currents.pptx");
	        XMLSlideShow ppt = new XMLSlideShow(is);
	        
	        for(XSLFSlide slide : ppt.getSlides()) {
	        	parano=1;
	        	List<XSLFShape> shapeList=Collections.emptyList();
	        	shapeList=slide.getShapes();
	            for(XSLFShape shape : shapeList){
	                if(shape instanceof XSLFTextShape) {
	                    XSLFTextShape txShape = (XSLFTextShape)shape;
	                    //text=txShape.getText();
	                    //out.println(txShape.getText());
	                   for( XSLFTextParagraph para :txShape.getTextParagraphs())
	                   {
	                	   if(parano==1)
	                	   {
	                		   String heading=para.getText();
	                		   for(String h : must)
	                		   {
	                			   if(heading.toLowerCase()==h.toLowerCase())
	                			   {
	                				   must.remove(h);
	                				   break;
	                			   }
	                		   }
	                		   
	                		   parano++;
	                	   }
	                   }
	                }
	                slideno++;
	            }
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		for(String h : must)
		{
			returntext+="Heading "+h+" is missing.\n";
		}
		return returntext;
	}
	
	public String checkmustnotHeadings(List<String> mustnot)
	{
		int slideno=1,parano=1;
		String returntext="";
		try
		{
			FileInputStream is = new FileInputStream(path);
			//FileInputStream is = new FileInputStream("F:/IITB/Ocean currents.pptx");
	        XMLSlideShow ppt = new XMLSlideShow(is);
	        
	        for(XSLFSlide slide : ppt.getSlides()) {
	        	parano=1;
	        	List<XSLFShape> shapeList=Collections.emptyList();
	        	shapeList=slide.getShapes();
	            for(XSLFShape shape : shapeList){
	                if(shape instanceof XSLFTextShape) {
	                    XSLFTextShape txShape = (XSLFTextShape)shape;
	                    //text=txShape.getText();
	                    //out.println(txShape.getText());
	                   for( XSLFTextParagraph para :txShape.getTextParagraphs())
	                   {
	                	   if(parano==1)
	                	   {
	                		   String heading=para.getText();
	                		   
	                		   for(String h : mustnot)
	                		   {
	                			   if(heading.toLowerCase()==h.toLowerCase())
	                			   {
	                				   returntext+="Slide "+Integer.toString(slideno)+" has the heading "+h+"\n";
	                				   break;
	                			   }   
	                		   }
	                		   parano++;
	                	   }
	                   }
	                }
	                slideno++;
	            }
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return returntext;
	}
	public Ppt(String v)
	{
        String[] subDirs = new String[10];
        v=new File(v).getAbsolutePath();
        String filename=new File(v).getName()+".pptx";
		location=new File(v,"presentation").getAbsolutePath();
		System.out.println(location);
		System.out.println(filename);
		path=new File(location,filename).getAbsolutePath();
        System.out.println(path);
                
	}
	
	public String imageCheck(int PPI)
	{
		FileInputStream is;
		String result="";
		int slide=1;
		try {
			is = new FileInputStream(path);
			XMLSlideShow ppt = new XMLSlideShow(is);
	        is.close();
	        
	        for (XSLFPictureData img : ppt.getPictureData()) 
	        {
	        	Dimension i=img.getImageDimension();
	        	Dimension p =img.getImageDimensionInPixels();
	        	double pheight=p.getHeight(),pwidth=p.getWidth();
	        	double iheight=i.getHeight(),iwidth=i.getWidth();
	        	double pnttoinch=0.0138889;
	        	iheight*=pnttoinch;
	        	iwidth*=pnttoinch;
	        	double pdiagonal,idiagonal;
	        	pdiagonal=Math.sqrt(((pheight*pheight)+(pwidth*pwidth)));
	        	idiagonal=Math.sqrt(((iheight*iheight)+(iwidth*iwidth)));
	        	double ppi=pdiagonal/idiagonal;
	        	System.out.println(ppi);
	        	if(Math.ceil(ppi) <PPI)
	        	{
	        		String res="The Image on slide "+Integer.toString(slide)+" is less than the specified image quality.\n"; 
	        		result+=res;
	        	}
	        	slide++;
	        }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals(""))
		{
			result="All images satisfy the specified image quality";
		}
		return result;
        
		
	}
	public static void main(String args[])
	{
		//getNotes(9);
		//String v=getBullets();
		//AdjustFont(32,20);
	}
}