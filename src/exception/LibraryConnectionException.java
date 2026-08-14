package exception;

public class LibraryConnectionException extends RuntimeException {
    public LibraryConnectionException(String message) {
        super(message);
    }

    public LibraryConnectionException(int roadId, int libraryId) {
        super("Road " + roadId + " is not connected to library " + libraryId + ".");
    }
}