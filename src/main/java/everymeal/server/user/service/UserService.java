package everymeal.server.user.service;


import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailAuthVerifyReq;
import everymeal.server.user.controller.dto.request.UserSingReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;

public interface UserService {

    Boolean signUp(UserSingReq userDeviceId);

    UserLoginRes login(UserSingReq request);

    Boolean isAuth(AuthenticatedUser authenticatedUser);

    UserEmailAuthRes emailAuth(UserEmailAuthReq request, AuthenticatedUser authenticatedUser);

    Boolean verifyEmailAuth(UserEmailAuthVerifyReq request, AuthenticatedUser authenticatedUser);

    Boolean checkRegistration(String userDeviceId);

}
