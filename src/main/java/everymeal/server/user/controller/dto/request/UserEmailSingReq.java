package everymeal.server.user.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserEmailSingReq {

    @Pattern(regexp = "^[^\\\\s]{1,10}$", message = "닉네임은 1~10자리로 입력해주세요.")
    @Schema(description = "이메일", example = "test@naver.com")
    private String nickname;

    @Schema(
        description = "이메일 인증 API에서 반환된 토큰 값",
        defaultValue = "eyBdvsjfgsdkgb",
        example = "eyBdvsjfgsdkgb")
    @NotBlank(message = "이메일 인증 토큰을 입력해주세요.")
    private String emailAuthToken;

    @Schema(description = "이메일 유저가 입력한 인증 값", defaultValue = "123456", example = "123456")
    @NotBlank(message = "인증번호를 입력해주세요.")
    private String emailAuthValue;

    @Schema(description = "선택 대학교 IDX", example = "1", defaultValue = "1")
    @NotBlank(message = "대학교를 선택해주세요.")
    private Long universityIdx;

    @Schema(description = "프로필 이미지 URL", example = "https://everymeal.s3.ap-northeast-2.amazonaws.com/1627667445.png")
    @NotBlank(message = "프로필 이미지를 선택해주세요.")
    private String profileImgKey;

}
