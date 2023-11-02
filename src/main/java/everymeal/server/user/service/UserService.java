package everymeal.server.user.service;


import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;

public interface UserService {

    Boolean signUp(UserEmailSingReq userDeviceId);

    UserLoginRes login(UserEmailLoginReq request);

    Boolean isAuth(AuthenticatedUser authenticatedUser);

    UserEmailAuthRes emailAuth(UserEmailAuthReq request, AuthenticatedUser authenticatedUser);

    Boolean verifyEmailAuth(UserEmailLoginReq request, AuthenticatedUser authenticatedUser);
}
