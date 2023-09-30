package everymeal.server.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionList {
    MEAL_NOT_FOUND("M0001", HttpStatus.NOT_FOUND, "Meal Not Found"),
    RESTAURANT_NOT_FOUND("M0002", HttpStatus.NOT_FOUND, "등록된 식당이 아닙니다."),
    UNIVERSITY_NOT_FOUND("M0003", HttpStatus.NOT_FOUND, "등록된 학교가 아닙니다."),
    INVALID_MEAL_OFFEREDAT_REQUEST(
            "M0004", HttpStatus.BAD_REQUEST, "등록되어 있는 식단 데이터 보다 과거의 날짜로 등록할 수 없습니다."),
    INVALID_REQUEST("R0001", HttpStatus.BAD_REQUEST, "Request의 Data Type이 올바르지 않습니다."),

    USER_NOT_FOUND("U0001", HttpStatus.NOT_FOUND, "등록된 유저가 아닙니다."),
    USER_AUTH_TIME_OUT("U0002", HttpStatus.FORBIDDEN, "인증 시간이 만료되었습니다."),
    USER_AUTH_FAIL("U0003", HttpStatus.FORBIDDEN, "인증에 실패하였습니다."),

    TOKEN_NOT_VALID("T0001", HttpStatus.NOT_ACCEPTABLE, "해당 토큰은 유효하지 않습니다."),
    TOKEN_EXPIRATION("T0002", HttpStatus.FORBIDDEN, "토큰이 만료되었습니다."),
    ;

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
