package everymeal.server.user.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSingReq {

    @Schema(description = "유저의 기기 고유 번호", defaultValue = "123456789", example = "1234567890")
    private String deviceId;
}
