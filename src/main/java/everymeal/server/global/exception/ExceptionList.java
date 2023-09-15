package everymeal.server.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionList {
    MEAL_NOT_FOUND("M0001", HttpStatus.NOT_FOUND, "Meal Not Found"),
    RESTAURANT_NOT_FOUND("M0002", HttpStatus.NOT_FOUND, "등록된 식당이 아닙니다."),
    INVALID_REQUEST("R0001", HttpStatus.BAD_REQUEST, "Request의 Data Type이 올바르지 않습니다."),

    USER_NOT_FOUND("U0001", HttpStatus.NOT_FOUND, "등록된 유저가 아닙니다."),
    ;

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
