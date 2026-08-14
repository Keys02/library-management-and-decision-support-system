package exception;

public class InvalidWeightTypeException extends RuntimeException {
    public InvalidWeightTypeException(String type) {
        super("Unknown weight type: " + type);
    }
}