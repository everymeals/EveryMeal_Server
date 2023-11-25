package everymeal.server.user.service;


import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.request.UserProfileUpdateReq;
import everymeal.server.user.controller.dto.request.WithdrawalReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.controller.dto.response.UserProfileRes;

public interface UserService {

    UserLoginRes signUp(UserEmailSingReq request);

    UserLoginRes login(UserEmailLoginReq request);

    UserEmailAuthRes emailAuth(UserEmailAuthReq request);

    Boolean verifyEmailAuth(String emailAuthToken, String emailAuthValue);

    Boolean checkUser(String email);

    UserProfileRes getUserProfile(AuthenticatedUser authenticatedUser);

    Boolean updateUserProfile(
            AuthenticatedUser authenticatedUser, UserProfileUpdateReq userProfileUpdateReq);

    Boolean withdrawal(AuthenticatedUser authenticatedUser, WithdrawalReq request);
}
