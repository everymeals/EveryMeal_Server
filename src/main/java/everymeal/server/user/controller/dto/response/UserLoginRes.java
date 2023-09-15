package everymeal.server.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserLoginRes {

    private String accessToken;
    @JsonInclude(Include.NON_NULL)
    @Schema(hidden = true)
    private String refreshToken;
}
