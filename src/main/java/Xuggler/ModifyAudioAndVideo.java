/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Xuggler;

/**
 *
 * @author Joey
 */
/*******************************************************************************
 * Copyright (c) 2008, 2010 Xuggle Inc.  All rights reserved.
 *  
 * This file is part of Xuggle-Xuggler-Main.
 *
 * Xuggle-Xuggler-Main is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Xuggle-Xuggler-Main is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Xuggle-Xuggler-Main.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.nio.ShortBuffer;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IRational;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.imageio.ImageIO;


/**
 * Read and modify audio and video frames and use the {@link
 * IMediaWriter} to encode that media and write it out to a file.
 *
 * @author trebor
 * @author aclarke
 */
public class ModifyAudioAndVideo
{
  // the log


  private static final Logger log = LoggerFactory.getLogger(
    ModifyAudioAndVideo.class);
  { log.trace("<init>"); }


  /**
   * Create and display a number of bouncing balls on the 
   */


  public static void main(String[] args)
  {
      try {
//    if (args.length < 2)
//    {
//      System.out.println(
//        "Usage: ModifyAudioAndVideo <inputFileName> <outputFileName>");
//      System.exit(-1);
//    }
//args[0]="1.avi";
//args[1]="2.avi";
          
          File inputFile = new File("1.avi");
          if (!inputFile.exists())
          {
              System.out.println("Input file does not exist: " + inputFile);
              System.exit(-1);
          }
          
          
          File outputFile = new File("2.avi");
          
          IMediaWriter out=ToolFactory.makeWriter("test.mp4");    // create a media reader and configure it to generate BufferImages
          out.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, 720, 540);
          BufferedImage old = ImageIO.read(new File("test.jpg"));
         // BufferedImage img= new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
              for(int i=0;i<10;i++)      
              out.encodeVideo(0, convertToType(old, BufferedImage.TYPE_3BYTE_BGR),10, TimeUnit.SECONDS);
         
              out.close();
              
//
//    IMediaReader reader = ToolFactory.makeReader(inputFile.toString());
//    reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
//
//
//    // create a writer and configure it's parameters from the reader
//    
//    IMediaWriter writer = ToolFactory.makeWriter(outputFile.toString(), reader);
//
//     
//    // create a tool which paints video time stamp into frame
//
//
//    IMediaTool addTimeStamp = new TimeStampTool();
//
//
//    // create a tool which reduces audio volume to 1/10th original
//
//
//    IMediaTool reduceVolume = new VolumeAdjustTool(0.1);
//
//
//    // create a tool chain:
//    //   reader -> addTimeStamp -> reduceVolume -> writer
//
//
//    reader.addListener(addTimeStamp);
//    addTimeStamp.addListener(reduceVolume);
//    reduceVolume.addListener(writer);
//
//
//    // add a viewer to the writer, to see media modified media
//    
//    writer.addListener(ToolFactory.makeViewer());
//
//
//    // read and decode packets from the source file and
//    // then encode and write out data to the output file
//    
//    while (reader.readPacket() == null)
//      do {} while(false);
          
      } catch (IOException ex) {
          java.util.logging.Logger.getLogger(ModifyAudioAndVideo.class.getName()).log(Level.SEVERE, null, ex);
          ex.printStackTrace();
      }
  }  


  /** 
   * Create a tool which adds a time stamp to a video image.
   */


  static class TimeStampTool extends MediaToolAdapter
  {
    /** {@inheritDoc} */


    @Override
      public void onVideoPicture(IVideoPictureEvent event)
    {
      // get the graphics for the image


      Graphics2D g = event.getImage().createGraphics();


      // establish the timestamp and how much space it will take


      String timeStampStr = event.getPicture().getFormattedTimeStamp();
      Rectangle2D bounds = g.getFont().getStringBounds(timeStampStr,
        g.getFontRenderContext());
      
      // compute the amount to inset the time stamp and translate the
      // image to that position


      double inset = bounds.getHeight() / 2;
      g.translate(inset, event.getImage().getHeight() - inset);
      
      // draw a white background and black timestamp text


      g.setColor(Color.WHITE);
      g.fill(bounds);
      g.setColor(Color.BLACK);
      g.drawString(timeStampStr, 0, 0);
      
      // call parent which will pass the video onto next tool in chain


      super.onVideoPicture(event);
    }
  }


  /** 
   * Create a tool which adjusts the volume of audio by some constant factor.
   */


  static class VolumeAdjustTool extends MediaToolAdapter
  {
    // the amount to adjust the volume by


    private double mVolume;
    
    /** 
     * Construct a volume adjustor.
     * 
     * @param volume the volume muliplier, values between 0 and 1 are
     *        recommended.
     */


    public VolumeAdjustTool(double volume)
    {
      mVolume = volume;
    }


    /** {@inheritDoc} */


    @Override
      public void onAudioSamples(IAudioSamplesEvent event)
    {
      // get the raw audio byes and adjust it's value 
      
      ShortBuffer buffer = event.getAudioSamples().getByteBuffer().asShortBuffer();
      for (int i = 0; i < buffer.limit(); ++i)
        buffer.put(i, (short)(buffer.get(i) * mVolume));
      
      // call parent which will pass the audio onto next tool in chain


      super.onAudioSamples(event);
    }
  }
     public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        } 
        else { 
            image = new BufferedImage(sourceImage.getWidth(),
            sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        } 
        return image;
    } 
}

