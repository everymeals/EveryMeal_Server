package everymeal.server.dev.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public record LoginRes(
        String accessToken,
        String nickname,
        String profileImg,
        @JsonInclude(Include.NON_NULL) @Schema(hidden = true) String refreshToken) {
    public LoginRes withoutRefreshToken() {
        return new LoginRes(accessToken, nickname, profileImg, null);
    }
}
