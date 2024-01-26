package everymeal.server.global.util.swagger;


import everymeal.server.global.exception.ApplicationException;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionList implements BaseExceptionList {
    USER_NOTFOUND("USR0001", HttpStatus.NOT_FOUND, "등록된 유저가 아닙니다."),
    ;

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;

    @Override
    public ApplicationException getErrorReason() {
        return new ApplicationException(getCODE(), getHttpStatus(), getMESSAGE());
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getMESSAGE();
    }
}
