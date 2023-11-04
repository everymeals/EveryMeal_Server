package everymeal.server.user.service;


import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;

public interface UserService {

    UserLoginRes signUp(UserEmailSingReq userDeviceId);

    UserLoginRes login(UserEmailLoginReq request);

    UserEmailAuthRes emailAuth(UserEmailAuthReq request);

    Boolean verifyEmailAuth(UserEmailLoginReq request);

    Boolean checkUser(String email);
}
