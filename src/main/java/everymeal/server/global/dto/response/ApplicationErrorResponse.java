package everymeal.server.global.dto.response;

import everymeal.server.global.exception.ApplicationException;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Builder
@ResponseBody
public class ApplicationErrorResponse<T>{
    @Schema(title = "성공여부", example = "false", description = "성공여부", defaultValue = "false")
    private Boolean isSuccess;
    @Schema(title = "에러메세지", example = "T0001", description = "에러메세지", defaultValue = "T0001")
    private String errorCode;
    @Schema(title = "에러발생시간", example = "2021-08-01T00:00:00", description = "에러발생시간", defaultValue = "2021-08-01T00:00:00")
    private LocalDateTime localDateTime;
    @Schema(title = "에러메세지", example = "에러가 발생했습니다.", description = "에러메세지", defaultValue = "에러가 발생했습니다.")
    private String message;

    public static <T> ApplicationErrorResponse<T> error(ApplicationException e){
        return error(e.getErrorCode(), e.getMessage());
    }

    public static <T> ApplicationErrorResponse<T> error(String errorCode, String message) {
        return (ApplicationErrorResponse<T>) ApplicationErrorResponse.builder()
                .isSuccess(false)
                .errorCode(errorCode)
                .localDateTime(LocalDateTime.now())
                .message(message)
                .build();
    }

}
