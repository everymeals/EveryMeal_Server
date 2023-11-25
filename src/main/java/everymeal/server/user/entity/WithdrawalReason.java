package everymeal.server.user.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WithdrawalReason {
    NOT_USE_USUALLY("앱을 잘 쓰지 않아요"),
    INCONVENIENT_IN_TERMS_OF_USABILITY("사용성이 불편해요"),
    ERRORS_OCCUR_FREQUENTLY("오류가 자주 발생해요"),
    MY_SCHOOL_HAS_CHANGED("학교가 바뀌었어요"),
    ETC("기타");

    public final String MESSAGE;
}
