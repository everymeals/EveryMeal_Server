package everymeal.server.user.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.MailUtil;
import everymeal.server.global.util.aws.S3Util;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
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

    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final MailUtil mailUtil;
    private final S3Util s3Util;

    @Override
    @Transactional
    public UserLoginRes signUp(UserEmailSingReq request) {
        String emailTokenFromAuthCode = jwtUtil.getEmailTokenFromAuthCode(request.emailAuthToken());
        if (!request.emailAuthValue().equals(emailTokenFromAuthCode)) {
            throw new ApplicationException(ExceptionList.USER_AUTH_FAIL);
        }
        String email = jwtUtil.getEmailTokenFromEmail(request.emailAuthToken());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ApplicationException(ExceptionList.USER_ALREADY_EXIST);
        }
        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new ApplicationException(ExceptionList.NICKNAME_ALREADY_EXIST);
        }

        University university =
                universityRepository
                        .findById(request.universityIdx())
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.UNIVERSITY_NOT_FOUND));
        User user =
                User.builder()
                        .email(email)
                        .nickname(request.nickname())
                        .profileImgUrl(request.profileImgKey())
                        .university(university)
                        .build();
        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user.getIdx());
        String refreshToken = jwtUtil.generateRefreshToken(user.getIdx(), accessToken);
        return new UserLoginRes(
                accessToken,
                user.getNickname(),
                s3Util.getImgUrl(user.getProfileImgUrl()),
                refreshToken);
    }

    @Override
    public UserLoginRes login(UserEmailLoginReq request) {
        String emailTokenFromAuthCode = jwtUtil.getEmailTokenFromAuthCode(request.emailAuthToken());
        if (!request.emailAuthValue().equals(emailTokenFromAuthCode)) {
            throw new ApplicationException(ExceptionList.USER_AUTH_FAIL);
        }
        String email = jwtUtil.getEmailTokenFromEmail(request.emailAuthToken());
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        String accessToken = jwtUtil.generateAccessToken(user.getIdx());
        String refreshToken = jwtUtil.generateRefreshToken(user.getIdx(), accessToken);
        return new UserLoginRes(
                accessToken,
                user.getNickname(),
                s3Util.getImgUrl(user.getProfileImgUrl()),
                refreshToken);
    }

    @Override
    public UserEmailAuthRes emailAuth(UserEmailAuthReq request) {
        try {
            Random random = SecureRandom.getInstanceStrong();
            int authCode = random.nextInt(900000) + 100000;
            String htmlText =
                    String.format(
                            """
                        <html>
                        <head>
                            <title>회원가입 인증 이메일</title>
                        </head>
                        <body>
                            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                                <h2>에브리밀 회원가입 인증 이메일</h2>
                                <p>안녕하세요,</p>
                                <p>회원가입을 완료하려면 아래의 인증번호를 입력해주세요:</p>
                                <div style="background-color: #f5f5f5; padding: 15px; text-align: center;">
                                    <h3>인증번호: <span style="color: #CC3939;">%s</span></h3>
                                </div>
                                <p>에브리밀은 이메일 인증을 완료하여 식당 및 학식 정보를 더욱 편리하게 이용할 수 있습니다.</p>
                                <p>에브리밀 서비스에 가입해주셔서 감사합니다.</p>
                                <p>감사합니다.</p>
                                <img src="https://github.com/everymeals/EveryMeal_Server/assets/53048655/b4543167-a03b-435d-b326-105aeeff3d6c" alt="에브리밀 로고" width="400px" height="150px">
                            </div>
                        </body>
                        </html>
                        """,
                            authCode);

            mailUtil.sendMail(request.getEmail(), "[에브리밀] 대학교 이메일 인증", htmlText);
            String mailJwt =
                    jwtUtil.generateEmailToken(request.getEmail(), Integer.toString(authCode));
            return UserEmailAuthRes.builder().emailAuthToken(mailJwt).build();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean verifyEmailAuth(String emailAuthToken, String emailAuthValue) {
        String emailTokenFromAuthCode = jwtUtil.getEmailTokenFromAuthCode(emailAuthToken);
        if (!emailAuthValue.equals(emailTokenFromAuthCode)) {
            throw new ApplicationException(ExceptionList.USER_AUTH_FAIL);
        }
        return true;
    }

    @Override
    public Boolean checkUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
