package everymeal.server.user.controller.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEmailAuthRes {

    private String emailAuthToken;
}
