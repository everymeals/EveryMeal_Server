package everymeal.server.user.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserEmailAuthReq {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;
}
