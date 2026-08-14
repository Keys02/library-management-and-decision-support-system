package exception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(String message) {
        super(message);
    }

    public BookUnavailableException(int bookId) {
        super("Book with id " + bookId + " is already borrowed.");
    }
}