package everymeal.server.global.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public ApplicationException(ExceptionList exceptionList) {
        super(exceptionList.getMESSAGE());
        this.errorCode = exceptionList.getCODE();
        this.httpStatus = exceptionList.getHttpStatus();
    }
}
