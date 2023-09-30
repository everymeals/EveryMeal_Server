package everymeal.server.user.controller.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileCreateReq {

    private String nickName;
    private String profileImage;
}
