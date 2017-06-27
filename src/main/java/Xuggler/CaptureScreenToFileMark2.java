package Xuggler;

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
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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

import gui.Call;

public class CaptureScreenToFileMark2 {
	
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
	private ProcessBufferedImage mProcessBufferedImage;
	private State mState;
	
	/*
	 * PROCESS BUFFERED IMAGE CLASS
	 */
	public enum State {
		IDLE,
		RUNNING
	}
	
	public class ProcessBufferedImage {
		
		private Queue<String> mImageQueue;
		private Dictionary mScaledImageDictionary;
		private File mTempFolder;
		private ExecutorService mExecutor;
		private BufferedImage mRawShot;
		
		public ProcessBufferedImage() {
			mExecutor = Executors.newFixedThreadPool(1);
			mImageQueue = new LinkedList<String>();
			mState = State.IDLE;
			
			// create a new temporary file
			mTempFolder = new File(System.getProperty("java.io.tmpdir"), "temp_images_for_video_capture");
			if(mTempFolder.exists()) {
				mTempFolder.delete();
			}
			mTempFolder.mkdir();
		}
		
		public void addImage(BufferedImage image) {
			
			String imagePath = com.iitb.lokavidya.core.utils.FileUtils.getNewUniqueFileName("jpeg");
		    try {
		    	FileOutputStream fout = new FileOutputStream(imagePath);
		        ImageIO.write(image, "jpeg", fout);
		        fout.close();
		        mImageQueue.add(imagePath);
				if(mState == State.IDLE) {
					// queue was empty before, so start the BackgroundProcessRunnable again
					System.out.println("queue was empty before, so start the BackgroundProcessRunnable again");
					(new Thread(new BackgroundProcessRunnable())).start();
				}
		    } catch(IOException e) {
		        // This *shouldn't* happen with a ByteArrayOutputStream, but if it
		        // somehow does happen, then we don't want to just ignore it
		    	e.printStackTrace();
		        throw new RuntimeException(e);
		    }
		}
		
		public class BackgroundProcessRunnable implements Runnable {
			
			public void onPreExecute() {
				mState = State.RUNNING;
			}
			
			public void run() {
				System.out.println("run called 1.");
				long startTime = System.currentTimeMillis();
				onPreExecute();
				
				// Runnable logic
			    try {
			        mRawShot =  ImageIO.read(new File(mImageQueue.poll()));
			    } catch (IOException e) {
			        e.printStackTrace();
			        return;
			    }
			    
			    System.out.println("run called 2.");
				
				// scale the image to fit the window width and window height
				int hVideo = Call.workspace.videoHeight;
				int wVideo = Call.workspace.videoWidth;
				int hShot = mRawShot.getHeight();
				int wShot = mRawShot.getWidth();

				int newShotHeight;
				int newShotWidth;
				
				System.out.println("run called 3.");
				
				if((hShot > hVideo) || (wShot > wVideo)) {
					// scale the image down
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
							newShotWidth = wVideo;
							newShotHeight = (int) Math.ceil(wVideo * hShot / wShot);
						} else {
							// the shot height should be the same as window height
							newShotHeight = hVideo;
							newShotWidth = (int) Math.ceil(hVideo * wShot / hShot);
						}
					}
				} else {
					// scale the image up
					if(((int) Math.ceil(hVideo * wShot / hShot)) <= wVideo) {
						// scale height to the maximum
						newShotHeight = hVideo;
						newShotWidth = (int) Math.ceil(hVideo * wShot / hShot);
					} else {
						// scale width to maximum
						newShotWidth = wVideo;
						newShotHeight = (int) Math.ceil(wVideo * hShot / wShot);
					}
				}
				
				Image scaledShot = mRawShot.getScaledInstance(newShotWidth, newShotHeight, Image.SCALE_SMOOTH);
					
				System.out.println("run called 4.");
				BufferedImage videoShot = new BufferedImage(Call.workspace.videoWidth, Call.workspace.videoHeight,
						BufferedImage.TYPE_INT_RGB);
				System.out.println("run called 5.");
				Graphics g = videoShot.getGraphics();
				System.out.println("run called 6.");
				int newx = (Call.workspace.videoWidth - newShotWidth) / 2;
				int newy = (Call.workspace.videoHeight - newShotHeight) / 2;
				g.drawImage(scaledShot, newx, newy, null);
				g.dispose();
				System.out.println("run called 7.");
				encodeImage(videoShot);
				System.out.println("run called 8.");
				long runTime = System.currentTimeMillis() - startTime;
				System.out.println("ProcessImage : runTime : " + runTime + "ms");
				System.out.println("image queue size : " + mImageQueue.size());
				
				onPostExecute();
			}
			
			public void onPostExecute() {
				if(mImageQueue.size() != 0) {
					run();
				} else {
					mState = State.IDLE;
				}
			}
			
		}
	}
	
	/*
	 * CAPTURE SCREEN TO FILE
	 */

	public CaptureScreenToFileMark2(String outFile) {
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
		mProcessBufferedImage = new ProcessBufferedImage();
	}

	public String currentImageUrl = "";
	public BufferedImage cachedRawShot = null;
	public Image cursor = null;
	public Rectangle rect = null;
	public boolean takeSingleSnapshot() {
		long startTime = System.currentTimeMillis();
		BufferedImage videoShot;
		try {
			
			BufferedImage rawShot;
			if ((Call.workspace.isVisible()) && (Call.workspace.isActive())) {
				// if Lokavidya is the foreground activity, then capture the slide as a frame in the video
				System.out.println("takeSingleSnapshot : slide.getImageUrl() : " + Call.workspace.currentSegment.getSlide().getImageURL());
				String imageUrl = Call.workspace.currentSegment.getSlide().getImageURL();
				if(!imageUrl.equals(currentImageUrl) && cachedRawShot != null) {
					System.out.println("creating new rawshot");
					rawShot = UIUtils.createBufferedImage(imageUrl);
				} else {
					System.out.println("using cached rawshot");
					rawShot = cachedRawShot;
				}
			} else {
				// capture the screen as a frame in the video
				if(rect == null) {
					if (Call.workspace.recordingWidth != 0) {
						rect = new Rectangle(Call.workspace.x, Call.workspace.y, Call.workspace.recordingWidth,
								Call.workspace.recordingHeight);
					} else {
						rect = new Rectangle(this.screenBounds);
					}
				}
				
				long timeBeforeTakingRawshot = System.currentTimeMillis() - startTime;
				System.out.println("timeBeforeTakingRawshot : " + timeBeforeTakingRawshot + "ms");
				rawShot = robot.createScreenCapture(rect);
				int x = MouseInfo.getPointerInfo().getLocation().x;
				int y = MouseInfo.getPointerInfo().getLocation().y;
				
				// TODO
				// AFTER THIS, EVERYTHING ELSE SHOULD BE DONE IN A BACKGROUND THREAD
				long timeBeforeDrawingCooltip = System.currentTimeMillis() - startTime;
				System.out.println("timeBeforeDrawingTooltip : " + timeBeforeDrawingCooltip + "ms");
				// draw a cool tooltip
				if(cursor == null) {
					cursor = ImageIO.read(new File("resources/drag_16x16.png"));
				}
				Graphics2D graphics2D = rawShot.createGraphics();
				graphics2D.drawImage(cursor, x - Call.workspace.x, y - Call.workspace.y, 48, 48, null); // cursor.gif is 48x48 size.
			}			
			
			try {
				mProcessBufferedImage.addImage(rawShot);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long runTime = System.currentTimeMillis() - startTime;
			System.out.println("captureScreenToFile : runtime : " + runTime + "ms");
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
	
	public void saveImage(BufferedImage originalImage) {
		File outputfile;
		try {
			outputfile = new File("capture.png");
			ImageIO.write(originalImage, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
	
	public void closeStreams() {
		int retval = outContainer.writeTrailer();
		if (retval < 0)
			throw new RuntimeException("Could not write trailer to output file");
	}
	
	public State getState() {
		return mState;
	}
	
	public long getDuration() {
		return (long) (1000 / frameRate.getDouble());
	}

	public int getFps() {
		return (int) (frameRate.getDouble());
	}

	public void setPauseTime(long pauseTime) {
		this.pauseTime = pauseTime;
	}

	public long durationOfVideo() {
		return seconds;
	}


}
