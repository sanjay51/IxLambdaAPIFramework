package IxLambdaBackend.exception;

public class UnknownOperationException extends RuntimeException {

    public UnknownOperationException(String message) {
        super(message);
    }
}