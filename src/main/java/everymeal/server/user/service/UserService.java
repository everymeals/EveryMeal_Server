package everymeal.server.user.service;


import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.response.UserLoginRes;

public interface UserService {

    Boolean signUp(String userDeviceId);

    UserLoginRes login(String userDeviceId);

    Boolean isAuth(AuthenticatedUser accessToken);
}
