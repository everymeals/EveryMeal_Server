package everymeal.server.user.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Boolean signUp(String userDeviceId) {
        User user = User.builder().deviceId(userDeviceId).build();
        userRepository.save(user);
        return true;
    }

    @Override
    public UserLoginRes login(String userDeviceId) {
        User user =
                userRepository
                        .findByDeviceId(userDeviceId)
                        .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        String accessToken = jwtUtil.generateAccessToken(user.getIdx());
        String refreshToken = jwtUtil.generateRefreshToken(user.getIdx(), accessToken);
        return UserLoginRes.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public Boolean isAuth(AuthenticatedUser authenticatedUser) {
        User user = userRepository.findById(authenticatedUser.getIdx())
            .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        return !user.getEmail().isEmpty();
    }
}
