package everymeal.server.user.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailAuthVerifyReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;

    @Override
    @Transactional
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
            String mailJwt =
                jwtUtil.generateEmailToken(
                    authenticatedUser.getIdx(), request.getEmail(), Integer.toString(authCode));
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setSubject("[에브리밀] 대학교 이메일 인증");
            mimeMessage.setText("인증번호 : " + authCode);
            mimeMessage.setRecipients(RecipientType.TO, request.getEmail());
            javaMailSender.send(mimeMessage);
            return UserEmailAuthRes.builder().emailAuthToken(mailJwt).build();
        } catch (MessagingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
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
