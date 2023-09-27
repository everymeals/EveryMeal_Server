package everymeal.server.user.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserEmailAuthVerifyReq {

    @Schema(
            description = "이메일 인증 API에서 반환된 토큰 값",
            defaultValue = "eyBdvsjfgsdkgb",
            example = "eyBdvsjfgsdkgb")
    private String emailAuthToken;

    @Schema(description = "이메일 유저가 입력한 인증 값", defaultValue = "123456", example = "123456")
    private String emailAuthValue;
}
