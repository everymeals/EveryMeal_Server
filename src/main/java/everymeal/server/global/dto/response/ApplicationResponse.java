package everymeal.server.global.dto.response;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationResponse<T> {

    private boolean isSuccess;
    private LocalDateTime localDateTime;
    private String message;
    private T data; // == body

    public static <T> ApplicationResponse<T> create(T data) {
        return makeResponse("created", data);
    }

    public static <T> ApplicationResponse<T> create(String message, T data) {
        return makeResponse(message, data);
    }

    public static <T> ApplicationResponse<T> ok() {
        return makeResponse("성공", null);
    }

    public static <T> ApplicationResponse<T> ok(T data) {
        return makeResponse("성공", data);
    }

    private static <T> ApplicationResponse<T> makeResponse(String message, T data) {
        return (ApplicationResponse<T>)
                ApplicationResponse.builder()
                        .isSuccess(true)
                        .localDateTime(LocalDateTime.now())
                        .message(message)
                        .data(data)
                        .build();
    }
}
