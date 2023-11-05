package everymeal.server.user.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginRes(
        String accessToken,
        String nickname,
        String profileImg,
        @JsonInclude(Include.NON_NULL) @Schema(hidden = true) String refreshToken) {
    public UserLoginRes withoutRefreshToken() {
        return new UserLoginRes(accessToken, nickname, profileImg, null);
    }
}
