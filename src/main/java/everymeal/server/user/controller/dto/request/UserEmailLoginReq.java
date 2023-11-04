package everymeal.server.user.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record UserEmailLoginReq(    @Schema(
    description = "이메일 인증 API에서 반환된 토큰 값",
    defaultValue = "eyBdvsjfgsdkgb",
    example = "eyBdvsjfgsdkgb")
                                    @NotBlank(message = "이메일 인증 토큰을 입력해주세요.")
                                    String emailAuthToken,

                                        @Schema(description = "이메일 유저가 입력한 인증 값", defaultValue = "123456", example = "123456")
    @NotBlank(message = "인증번호를 입력해주세요.")
    String emailAuthValue) {


}
