package Xuggler;

import java.io.File;

import Xuggler.ConcatenateAudioAndVideo.MediaConcatenator;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.AudioSamplesEvent;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.ICloseCoderEvent;
import com.xuggle.mediatool.event.ICloseEvent;
import com.xuggle.mediatool.event.IOpenCoderEvent;
import com.xuggle.mediatool.event.IOpenEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.VideoPictureEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IVideoPicture;

import static java.lang.System.out;
import static java.lang.System.exit;

public class Concatenate {
	
	public static void main(String args[])
	{
		concatenate("F:\\IITB\\Client2.0\\output1.vlc", "F:\\IITB\\Client2.0\\output.vlc", "F:\\IITB\\Client2.0\\join.vlc");
	}
	public static void concatenate(String sourceUrl1, String sourceUrl2,
		    String destinationUrl)
		  {
		    out.printf("transcode %s + %s -> %s\n", sourceUrl1, sourceUrl2,
		      destinationUrl);

		    //////////////////////////////////////////////////////////////////////
		    //                                                                  //
		    // NOTE: be sure that the audio and video parameters match those of //
		    // your input media                                                 //
		    //                                                                  //
		    //////////////////////////////////////////////////////////////////////

		    // video parameters

		    final int videoStreamIndex = 0;
		    final int videoStreamId = 0;
		    final int width =480;
		    final int height = 360;

		    // audio parameters

		    final int audioStreamIndex = 1;
		    final int audioStreamId = 0;
		    final int channelCount = 2;
		    final int sampleRate = 44100; // Hz

		    // create the first media reader

		    IMediaReader reader1 = ToolFactory.makeReader(sourceUrl1);

		    // create the second media reader

		    IMediaReader reader2 = ToolFactory.makeReader(sourceUrl2);
		    
		    IMediaWriter writer = ToolFactory.makeWriter(destinationUrl);

		    // create the media concatenator

		    //MediaConcatenator concatenator = new MediaConcatenator(audioStreamIndex,videoStreamIndex);

		    // concatenator listens to both readers

		    reader1.addListener(writer);
		    reader2.addListener(writer);

		    // create the media writer which listens to the concatenator

		    
		    //concatenator.addListener(writer);

		    // add the video stream

		    //writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);

		    // add the audio stream

		    //writer.addAudioStream(audioStreamIndex, audioStreamId, channelCount,sampleRate);

		    // read packets from the first source file until done

		    while (reader1.readPacket() == null)
		      ;

		    // read packets from the second source file until done

		    while (reader2.readPacket() == null)
		      ;

		    // close the writer

		    writer.close();
		  }
		  

}
