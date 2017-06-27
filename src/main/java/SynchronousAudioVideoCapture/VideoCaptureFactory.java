package SynchronousAudioVideoCapture;

public class VideoCaptureFactory {

	static VideoCapture getVideoCapture(String videoPath, int videoFrameRate, SynchronousAudioVideoCapture capture) {
		if (System.getProperty("os.name").contains("Linux")) {
			// Linux
			return new VideoCaptureUbuntu(videoPath, videoFrameRate, capture);
		} else {
			return new VideoCaptureNonUbuntu(videoPath, videoFrameRate, capture);
		}
	}
}
