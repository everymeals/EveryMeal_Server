package everymeal.server.user.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.MailUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailAuthVerifyReq;
import everymeal.server.user.controller.dto.request.UserSingReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final MailUtil mailUtil;

    @Override
    @Transactional
    public Boolean signUp(UserSingReq request) {
        User user = User.builder().deviceId(request.getDeviceId()).build();
        userRepository.save(user);
        return true;
    }

    @Override
    public UserLoginRes login(UserSingReq request) {
        User user =
                userRepository
                        .findByDeviceId(request.getDeviceId())
                        .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        String accessToken = jwtUtil.generateAccessToken(user.getIdx());
        String refreshToken = jwtUtil.generateRefreshToken(user.getIdx(), accessToken);
        return UserLoginRes.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public Boolean isAuth(AuthenticatedUser authenticatedUser) {
        User user =
                userRepository
                        .findByDeviceId(authenticatedUser.getDeviceId())
                        .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        return user.getEmail() != null;
    }

    @Override
    public UserEmailAuthRes emailAuth(
            UserEmailAuthReq request, AuthenticatedUser authenticatedUser) {
        try {
            Random random = SecureRandom.getInstanceStrong();
            int authCode = random.nextInt(900000) + 100000;
            String htmlText =
                    String.format(
                            "<html>"
                                    + "<body>"
                                    + "<p>안녕하세요!</p>"
                                    + "<p>에브리밀 서비스에 가입해주셔서 감사합니다. 이메일 인증을 완료하여 식당 및 학식 정보를 더욱 편리하게 이용할 수 있습니다.</p>"
                                    + "<p>아래의 인증 코드를 사용하여 이메일 인증을 진행해주세요:</p>"
                                    + "<p><strong>인증 코드: %s</strong></p>"
                                    + "<br />"
                                    + "<p>감사합니다.</p>"
                                    + "<br />"
                                    + "<img src=\"https://github.com/everymeals/EveryMeal_Server/assets/53048655/b4543167-a03b-435d-b326-105aeeff3d6c\" alt=\"에브리밀 로고\" width=\"200px\" height=\"75px\">"
                                    + "</body>"
                                    + "</html>",
                            authCode);

            mailUtil.sendMail(request.getEmail(), "[에브리밀] 대학교 이메일 인증", htmlText);
            String mailJwt =
                    jwtUtil.generateEmailToken(
                            authenticatedUser.getIdx(),
                            request.getEmail(),
                            Integer.toString(authCode));
            return UserEmailAuthRes.builder().emailAuthToken(mailJwt).build();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean verifyEmailAuth(
            UserEmailAuthVerifyReq request, AuthenticatedUser authenticatedUser) {
        String emailTokenFromAuthCode =
                jwtUtil.getEmailTokenFromAuthCode(request.getEmailAuthToken());
        if (request.getEmailAuthValue().equals(emailTokenFromAuthCode)) {
            User user =
                    userRepository
                            .findById(authenticatedUser.getIdx())
                            .orElseThrow(
                                    () -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
            user.setEmail(emailTokenFromAuthCode);
            return true;
        } else {
            throw new ApplicationException(ExceptionList.USER_AUTH_FAIL);
        }
    }
}
