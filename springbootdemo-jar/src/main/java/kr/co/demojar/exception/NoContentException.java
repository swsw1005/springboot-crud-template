package kr.co.demojar.exception;

public class NoContentException extends CustomException{
    public NoContentException(String message) {
        super(message);
    }

    public NoContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoContentException(Throwable cause) {
        super(cause);
    }

    protected NoContentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
