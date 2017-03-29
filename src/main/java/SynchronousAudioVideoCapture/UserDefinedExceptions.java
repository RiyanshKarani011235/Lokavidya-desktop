package SynchronousAudioVideoCapture;

public class UserDefinedExceptions {

	static class CouldNotStartRecordingException extends Exception {
		
		private String mMessageString;
		
		public CouldNotStartRecordingException(String message) {
			mMessageString = message;
		}
		
		@Override
		public String toString() {
			return mMessageString;
		}
		
	}
	
}
