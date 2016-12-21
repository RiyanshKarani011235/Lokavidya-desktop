package Xuggler;

import gui.Call;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.iitb.lokavidya.core.utils.UIUtils;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

/**
 * This demonstration application shows how to use Xuggler and Java to take
 * snapshots of your screen and encode them in a video.
 * 
 * <p>
 * You can find the original code <a href=
 * "http://flexingineer.blogspot.com/2009/05/encoding-video-from-series-of-images.html"
 * > here </a>.
 * </p>
 * 
 * @author Denis Zgonjanin
 * @author aclarke
 * 
 */

public class CaptureScreenToFile {
	private final IContainer outContainer;
	private final IStream outStream;
	private final IStreamCoder outStreamCoder;

	private final IRational frameRate;

	private final Robot robot;
	private final Toolkit toolkit;
	private final Rectangle screenBounds;
	private long seconds = 0;
	private long firstTimeStamp = -1;
	private long pauseTime = 0;

	/**
	 * Takes up to one argument, which just gives a file name to encode as. We
	 * guess which video codec to use based on the filename.
	 * 
	 * @param args
	 *            The filename to write to.
	 */

	/**
	 * Create the demonstration object with lots of defaults. Throws an
	 * exception if we can't get the screen or open the file.
	 * 
	 * @param outFile
	 *            File to write to.
	 */
	public long getDuration() {
		return (long) (1000 / frameRate.getDouble());
	}

	public int getFps() {
		return (int) (frameRate.getDouble());
	}

	public CaptureScreenToFile(String outFile) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		toolkit = Toolkit.getDefaultToolkit();
		screenBounds = new Rectangle(toolkit.getScreenSize());

		// Change this to change the frame rate you record at
		frameRate = IRational.make(Call.workspace.framerate, 1);

		outContainer = IContainer.make();

		int retVal = outContainer.open(outFile, IContainer.Type.WRITE, null);
		if (retVal < 0) {
			throw new RuntimeException("could not open output file");
		}

		ICodec codec = ICodec.guessEncodingCodec(null, null, outFile, null, ICodec.Type.CODEC_TYPE_VIDEO);
		if (codec == null) {
			throw new RuntimeException("could not guess a codec");
		}

		outStream = outContainer.addNewStream(codec);
		outStreamCoder = outStream.getStreamCoder();

		outStreamCoder.setNumPicturesInGroupOfPictures(30);
		outStreamCoder.setCodec(codec);

		outStreamCoder.setBitRate(25000);
		outStreamCoder.setBitRateTolerance(9000);

		int width = 0;
		int height = 0;

		width = Call.workspace.videoWidth;
		height = Call.workspace.videoHeight;

		outStreamCoder.setPixelType(IPixelFormat.Type.YUV420P);
		outStreamCoder.setHeight(height);
		outStreamCoder.setWidth(width);
		outStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
		outStreamCoder.setGlobalQuality(0);

		outStreamCoder.setFrameRate(frameRate);
		outStreamCoder.setTimeBase(IRational.make(frameRate.getDenominator(), frameRate.getNumerator()));

		retVal = outStreamCoder.open(null, null);
		if (retVal < 0)
			throw new RuntimeException("could not open input decoder");
		retVal = outContainer.writeHeader();
		if (retVal < 0)
			throw new RuntimeException("could not write file header");
	}

	/**
	 * Encode the given image to the file and increment our time stamp.
	 * 
	 * @param originalImage
	 *            an image of the screen.
	 */

	public void saveImage(BufferedImage originalImage) {
		File outputfile;
		try {
			outputfile = new File("capture.png");
			ImageIO.write(originalImage, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setPauseTime(long pauseTime) {
		this.pauseTime = pauseTime;
	}

	public long durationOfVideo() {
		return seconds;
	}

	public void encodeImage(BufferedImage originalImage) {
		BufferedImage worksWithXugglerBufferedImage = convertToType(originalImage, BufferedImage.TYPE_3BYTE_BGR);
		IPacket packet = IPacket.make();

		long now = System.currentTimeMillis();
		if (firstTimeStamp == -1)
			firstTimeStamp = now;

		IConverter converter = null;
		try {
			converter = ConverterFactory.createConverter(worksWithXugglerBufferedImage, IPixelFormat.Type.YUV420P);
		} catch (UnsupportedOperationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}

		long timeStamp = (now - firstTimeStamp) * 1000; // convert to
														// microseconds
		timeStamp = timeStamp - pauseTime * 1000;
		seconds = timeStamp / 1000000;
		// System.out.println("TimeStamp "+timeStamp);
		// System.out.println(""+mins+":"+seconds);
		IVideoPicture outFrame = converter.toPicture(worksWithXugglerBufferedImage, timeStamp);

		outFrame.setQuality(0);
		int retval = outStreamCoder.encodeVideo(packet, outFrame, 0);
		if (retval < 0)
			throw new RuntimeException("could not encode video");
		if (packet.isComplete()) {
			retval = outContainer.writePacket(packet);
			if (retval < 0)
				throw new RuntimeException("could not save packet to container");
		}

	}

	/**
	 * Close out the file we're currently working on.
	 */

	public void closeStreams() {
		int retval = outContainer.writeTrailer();
		if (retval < 0)
			throw new RuntimeException("Could not write trailer to output file");
	}

	/**
	 * Use the AWT robot to take a snapshot of the current screen.
	 * 
	 * @return a picture of the desktop
	 */

	public BufferedImage takeSingleSnapshot() {
		BufferedImage videoShot;
		try {
			Rectangle rect;
			BufferedImage rawShot;
			if ((Call.workspace.isVisible()) && (Call.workspace.isActive())) {
				// if Lokavidya is the foreground activity, then capture the slide as a frame in the video
				System.out.println("takeSingleSnapshot : slide.getImageUrl() : " + Call.workspace.currentSegment.getSlide().getImageURL());
				rawShot = UIUtils.createBufferedImage(Call.workspace.currentSegment.getSlide().getImageURL());

			} else {
				// capture the screen as a frame in the video
				if (Call.workspace.recordingWidth != 0) {
					rect = new Rectangle(Call.workspace.x, Call.workspace.y, Call.workspace.recordingWidth,
							Call.workspace.recordingHeight);
				} else {
					rect = new Rectangle(this.screenBounds);
				}
				rawShot = robot.createScreenCapture(rect);
				Image cursor = ImageIO.read(new File("resources/drag.png"));
				int x = MouseInfo.getPointerInfo().getLocation().x;
				int y = MouseInfo.getPointerInfo().getLocation().y;

				// draw a cool tooltip
				Graphics2D graphics2D = rawShot.createGraphics();
				graphics2D.drawImage(cursor, x, y, 30, 30, null); // cursor.gif is 16x16 size.
			}

			// scale the image to fit the window width and window height
			int hVideo = Call.workspace.videoHeight;
			int wVideo = Call.workspace.videoWidth;
			int hShot = rawShot.getHeight();
			int wShot = rawShot.getWidth();

			int newShotHeight;
			int newShotWidth;
			
			System.out.println("hShot : " + hShot);
			System.out.println("wShot : " + wShot);
			
			if((hShot > hVideo) || (wShot > wVideo)) {
				// scale the image down
				System.out.println("Scaling down");
				if((hShot > hVideo) && (wShot <= wVideo)) {
					// scale down the height
					newShotHeight = hVideo;
					newShotWidth = (int) Math.ceil(hVideo * wShot / hShot);
				} else if((hShot <= hVideo) && (wShot > wVideo)) {
					// scale down the width
					newShotWidth = wVideo;
					newShotHeight = (int) Math.ceil(wVideo * hShot / wShot);
				} else {
					// scale down both
					if((int) Math.ceil(hVideo * wShot / hShot) > wVideo) {
						// the shot width should be the same as window width
						System.out.println("00000 1 00000");
						newShotWidth = wVideo;
						newShotHeight = (int) Math.ceil(wVideo * hShot / wShot);
					} else {
						// the shot height should be the same as window height
						System.out.println("00000 2 00000");
						newShotHeight = hVideo;
						newShotWidth = (int) Math.ceil(hVideo * wShot / hShot);
					}
				}
			} else {
				// scale the image up
				if(((int) Math.ceil(hVideo * wShot / hShot)) > wVideo) {
					// scale height to the maximum
					System.out.println("scaling up image height to maximum");
					newShotHeight = hVideo;
					newShotWidth = (int) Math.ceil(hVideo * wShot / hShot);
				} else {
					// scale width to maximum
					System.out.println("Scaling up image width to maximum");
					newShotWidth = wVideo;
					newShotHeight = (int) Math.ceil(wVideo * hShot / wShot);
				}
			}
			
			System.out.println("scaled image width : " + newShotWidth);
			System.out.println("Scaled image height : " + newShotHeight);


//			int h, neww, w, newh;
//			h = rawShot.getHeight();
//			w = rawShot.getWidth();
//			newh = Call.workspace.videoHeight;
//			do {
//				neww = (int) Math.ceil(w * newh / h);
//				if (neww > Call.workspace.videoWidth) {
//					newh -= 5;
//				} else {
//					System.out.println("Encoded image");
//					break;
//				}
//			} while (newh > 0);
			Image scaledShot = rawShot.getScaledInstance(newShotWidth, newShotHeight, Image.SCALE_SMOOTH);

			videoShot = new BufferedImage(Call.workspace.videoWidth, Call.workspace.videoHeight,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = videoShot.getGraphics();
			int newx = (Call.workspace.videoWidth - newShotWidth) / 2;
			int newy = (Call.workspace.videoHeight - newShotHeight) / 2;
			g.drawImage(scaledShot, newx, newy, null);
			g.dispose();
			return videoShot;
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Convert a {@link BufferedImage} of any type, to {@link BufferedImage} of
	 * a specified type. If the source image is the same type as the target
	 * type, then original image is returned, otherwise new image of the correct
	 * type is created and the content of the source image is copied into the
	 * new image.
	 * 
	 * @param sourceImage
	 *            the image to be converted
	 * @param targetType
	 *            the desired BufferedImage type
	 * 
	 * @return a BufferedImage of the specifed target type.
	 * 
	 * @see BufferedImage
	 */

	public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;

		// if the source image is already the target type, return the source
		// image

		if (sourceImage.getType() == targetType)
			image = sourceImage;

		// otherwise create a new image of the target type and draw the new
		// image

		else {
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}

		return image;
	}

}