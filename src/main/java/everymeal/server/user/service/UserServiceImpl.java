package everymeal.server.user.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.MailUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.global.util.aws.S3Util;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.request.UserProfileUpdateReq;
import everymeal.server.user.controller.dto.request.WithdrawalReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.controller.dto.response.UserProfileRes;
import everymeal.server.user.entity.User;
import everymeal.server.user.entity.Withdrawal;
import everymeal.server.user.entity.WithdrawalReason;
import everymeal.server.user.repository.UserMapper;
import everymeal.server.user.repository.UserRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UniversityRepository universityRepository;
    private final JwtUtil jwtUtil;
    private final MailUtil mailUtil;
    private final S3Util s3Util;
    private final UserMapper userMapper;
    private final WithdrawalServiceImpl withdrawalServiceImpl;

    private final UserCommServiceImpl userCommServiceImpl;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserLoginRes signUp(UserEmailSingReq request) {
        String emailTokenFromAuthCode = jwtUtil.getEmailTokenFromAuthCode(request.emailAuthToken());
        if (!request.emailAuthValue().equals(emailTokenFromAuthCode)) {
            throw new ApplicationException(ExceptionList.USER_AUTH_FAIL);
        }
        String email = jwtUtil.getEmailTokenFromEmail(request.emailAuthToken());
        Optional<User> userOp = userCommServiceImpl.getUserOptionalEntityByEmail(email);
        if (userOp.isPresent()) {
            if (Boolean.TRUE.equals(userOp.get().getIsDeleted())) {
                throw new ApplicationException(ExceptionList.USER_ALREADY_DELETED);
            }
            throw new ApplicationException(ExceptionList.USER_ALREADY_EXIST);
        }
        if (userCommServiceImpl.getUserOptionalEntityByNickname(request.nickname()).isPresent()) {
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
        userCommServiceImpl.save(user);

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
        User user = userCommServiceImpl.getUserEntityByEmail(email);
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
        return userCommServiceImpl.getUserOptionalEntityByEmail(email).isPresent();
    }

    @Override
    public UserProfileRes getUserProfile(AuthenticatedUser authenticatedUser) {
        Map<String, Object> result = userMapper.getUserProfile(authenticatedUser.getIdx());
        String profileImgUrl = s3Util.getImgUrl((String) result.get("profileImgUrl"));
        return UserProfileRes.of(result, profileImgUrl);
    }

    @Override
    @Transactional
    public Boolean updateUserProfile(
            AuthenticatedUser authenticatedUser, UserProfileUpdateReq request) {
        User user = userCommServiceImpl.getUserEntity(authenticatedUser.getIdx());
        // 닉네임 중복 검사
        Optional<User> duplicatedNickName =
                userCommServiceImpl.getUserOptionalEntityByNickname(request.nickName());
        if (duplicatedNickName.isPresent() && user != duplicatedNickName.get()) {
            throw new ApplicationException(ExceptionList.NICKNAME_ALREADY_EXIST);
        }
        user.updateProfile(request.nickName(), request.profileImageKey());
        return true;
    }

    @Override
    @Transactional
    public Boolean withdrawal(AuthenticatedUser authenticatedUser, WithdrawalReq request) {
        User user = userCommServiceImpl.getUserEntity(authenticatedUser.getIdx());
        Withdrawal withdrawal;
        if (request.withdrawalReason() != WithdrawalReason.ETC) { // 기타를 제외한 경우
            withdrawal =
                    Withdrawal.builder()
                            .withdrawalReason(request.withdrawalReason())
                            .user(user)
                            .build();
        } else { // 기타를 선택한 경우
            withdrawal =
                    Withdrawal.builder()
                            .withdrawalReason(request.withdrawalReason())
                            .etcReason(request.etcReason())
                            .user(user)
                            .build();
        }
        withdrawalServiceImpl.save(withdrawal); // 탈퇴 관련 정보 저장
        user.setIsDeleted(); // 논리 삭제
        return true;
    }

    @Override
    public String reissueAccessToken(String refreshToken) {
        AuthenticatedUser authenticateUserFromRefreshToken =
                jwtUtil.getAuthenticateUserFromRefreshToken(refreshToken);
        if (authenticateUserFromRefreshToken == null) {
            throw new ApplicationException(ExceptionList.TOKEN_NOT_VALID);
        }
        User user =
                userRepository
                        .findById(authenticateUserFromRefreshToken.getIdx())
                        .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        return jwtUtil.generateAccessToken(user.getIdx());
    }

    @Override
    public Boolean isVerifyAccessToken(String accessToken) {
        try {
            AuthenticatedUser authenticateUserFromAccessToken =
                    jwtUtil.getAuthenticateUserFromAccessToken(accessToken);
            if (authenticateUserFromAccessToken == null) {
                throw new ApplicationException(ExceptionList.TOKEN_NOT_VALID);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
