package everymeal.server.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionList {
    MEAL_NOT_FOUND("MEL0001", HttpStatus.NOT_FOUND, "Meal Not Found"),
    RESTAURANT_NOT_FOUND("MEL0002", HttpStatus.NOT_FOUND, "등록된 식당이 아닙니다."),
    UNIVERSITY_NOT_FOUND("MEL0003", HttpStatus.NOT_FOUND, "등록된 학교가 아닙니다."),
    INVALID_MEAL_OFFEREDAT_REQUEST(
            "MEL0004", HttpStatus.BAD_REQUEST, "동일한 데이터를 갖는 식단 데이터가 이미 존재합니다."),
    INVALID_REQUEST("COM0001", HttpStatus.BAD_REQUEST, "Request의 Data Type이 올바르지 않습니다."),

    USER_NOT_FOUND("USR0001", HttpStatus.NOT_FOUND, "등록된 유저가 아닙니다."),
    USER_AUTH_TIME_OUT("USR0002", HttpStatus.FORBIDDEN, "인증 시간이 만료되었습니다."),
    USER_AUTH_FAIL("USR0003", HttpStatus.FORBIDDEN, "인증에 실패하였습니다."),
    USER_ALREADY_EXIST("USR0004", HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    NICKNAME_ALREADY_EXIST("USR0005", HttpStatus.CONFLICT, "이미 등록된 닉네임입니다."),

    TOKEN_NOT_VALID("TKN0001", HttpStatus.NOT_ACCEPTABLE, "해당 토큰은 유효하지 않습니다."),
    TOKEN_EXPIRATION("TKN0002", HttpStatus.FORBIDDEN, "토큰이 만료되었습니다."),

    REVIEW_NOT_FOUND("RV0001", HttpStatus.NOT_FOUND, "등록된 리뷰가 아닙니다."),
    REVIEW_UNAUTHORIZED("RV0002", HttpStatus.FORBIDDEN, "해당 리뷰에 대한 권한이 없습니다."),
    REVIEW_ALREADY_MARKED("RV0003", HttpStatus.CONFLICT, "이미 해당 리뷰에 대한 평가를 하였습니다."),
    REVIEW_MARK_NOT_FOUND("RV0004", HttpStatus.NOT_FOUND, "등록된 리뷰 평가가 아닙니다."),
    STORE_NOT_FOUND("STR0001", HttpStatus.NOT_FOUND, "등록된 가게가 아닙니다."),

    // report
    REPORT_NOT_FOUND("RPT0001", HttpStatus.NOT_FOUND, "등록된 신고가 아닙니다."),
    REPORT_ALREADY_EXIST("RPT0002", HttpStatus.CONFLICT, "이미 신고한 리뷰입니다."),
    REPORT_REVIEW_SELF("RPT0003", HttpStatus.FORBIDDEN, "자신의 리뷰를 신고할 수 없습니다."),
    REPORT_REVIEW_ALREADY("RPT0004", HttpStatus.CONFLICT, "이미 신고한 리뷰입니다."),

    // SERVER
    SERVER_ERROR("SRV0001", HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생하였습니다."),
    PATH_NOT_FOUND("SRV0002", HttpStatus.NOT_FOUND, "요청한 경로가 존재하지 않습니다."),
    ;

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
