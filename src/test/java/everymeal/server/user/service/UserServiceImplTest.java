package everymeal.server.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
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
import everymeal.server.user.entity.User;
import everymeal.server.user.entity.WithdrawalReason;
import everymeal.server.user.repository.UserMapper;
import everymeal.server.user.repository.UserRepository;
import everymeal.server.user.repository.WithdrawalRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

class UserServiceImplTest extends IntegrationTestSupport {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @MockBean private MailUtil mailUtil;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private S3Util s3Util;
    @Autowired private WithdrawalRepository withdrawalRepository;
    @Autowired private EntityManager em;

    @Autowired private UserMapper userMapper;

    @AfterEach
    void tearDown() {
        withdrawalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입을 진행한다.")
    @Test
    void singUp() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        // when
        UserLoginRes userLoginRes = userService.signUp(request);

        // then
        assertThat(userLoginRes.nickname()).isEqualTo(request.nickname());
        assertThat(userLoginRes.profileImg()).isEqualTo(s3Util.getImgUrl(request.profileImgKey()));
    }

    @DisplayName("회원가입에서 이미 탈퇴한 유저의 경우 에러가 발생한다.")
    @Test
    @Transactional
    void singUpWithdrawalUser() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");
        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());

        User user = userRepository.save(createUser("test@gmail.com", "nickname"));

        user.setIsDeleted();

        userRepository.flush();
        em.clear();
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        // when then
        ApplicationException applicationException =
                assertThrows(ApplicationException.class, () -> userService.signUp(request));

        assertEquals(
                applicationException.getErrorCode(), ExceptionList.USER_ALREADY_DELETED.getCODE());
    }

    @DisplayName("회원가입에서 이메일 토큰에서 추출한 값과 요청 값이 다를 경우 에러가 발생한다.")
    @Test
    void singUpEmailTokenNotMatched() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "67891", university.getIdx(), "imageKey");

        // when then
        ApplicationException applicationException =
                assertThrows(ApplicationException.class, () -> userService.signUp(request));

        assertEquals(applicationException.getErrorCode(), ExceptionList.USER_AUTH_FAIL.getCODE());
    }

    @DisplayName("회원가입에서 이미 존재한 이메일인 경우 에러가 발생한다. ")
    @Test
    void singUpEmailNotMatched() {
        // given
        userRepository.save(createUser("test@gmail.com", "nickname2"));
        userRepository.flush();
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        // when then
        ApplicationException applicationException =
                assertThrows(ApplicationException.class, () -> userService.signUp(request));

        assertEquals(
                applicationException.getErrorCode(), ExceptionList.USER_ALREADY_EXIST.getCODE());
    }

    @DisplayName("회원가입에서 이미 존재한 닉네임인 경우 에러가 발생한다. ")
    @Test
    void singUpAlreadyNickname() {
        // given
        userRepository.saveAndFlush(createUser("test2@gmail.com", "nickname"));

        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        // when then
        ApplicationException applicationException =
                assertThrows(ApplicationException.class, () -> userService.signUp(request));

        assertEquals(
                applicationException.getErrorCode(),
                ExceptionList.NICKNAME_ALREADY_EXIST.getCODE());
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() {
        // given
        userRepository.saveAndFlush(createUser("test@gmail.com", "nickname"));

        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");
        UserEmailLoginReq request = new UserEmailLoginReq(token, "12345");

        // when
        UserLoginRes userLoginRes = userService.login(request);

        // then
        assertThat(userLoginRes.nickname()).isEqualTo("nickname");
    }

    @DisplayName("로그인에서 추출한 토큰이 요청한 토큰과 다를 경우 에러가 발생한다.")
    @Test
    void loginTokenValueNotMatched() {
        // given
        userRepository.saveAndFlush(createUser("test@gmail.com", "nickname"));

        String token = jwtUtil.generateEmailToken("test@gmail.com", "67891");
        UserEmailLoginReq request = new UserEmailLoginReq(token, "12345");

        // when then
        ApplicationException applicationException =
                assertThrows(ApplicationException.class, () -> userService.login(request));

        assertEquals(applicationException.getErrorCode(), ExceptionList.USER_AUTH_FAIL.getCODE());
    }

    @DisplayName("로그인에서 이메일이 존재하지 않을 경우 에러가 발생한다.")
    @Test
    void loginEmailNotMatched() {
        // given
        userRepository.saveAndFlush(createUser("test@gmail.com", "nickname"));

        String token = jwtUtil.generateEmailToken("test2@gmail.com", "12345");
        UserEmailLoginReq request = new UserEmailLoginReq(token, "12345");

        // when then
        ApplicationException applicationException =
                assertThrows(ApplicationException.class, () -> userService.login(request));

        assertEquals(applicationException.getErrorCode(), ExceptionList.USER_NOT_FOUND.getCODE());
    }

    @DisplayName("이메일 인증을 진행한다.")
    @Test
    void emailAuth() {
        // given
        UserEmailAuthReq request = UserEmailAuthReq.builder().email("test@gmail.com").build();

        // when
        UserEmailAuthRes response = userService.emailAuth(request);

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("이메일로 전송된 인증 값이 토큰의 인증값과 일치한지 확인한다.")
    @Test
    void verifyEmailAuth() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        // when
        Boolean response = userService.verifyEmailAuth(token, "12345");

        // then
        assertThat(response).isTrue();
    }

    @DisplayName("이메일로 전송된 인증 값이 토큰의 인증값과 일치한지 않다면 에러가 발생한다.")
    @Test
    void verifyEmailAuthNotMatched() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "67891");

        // when then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> userService.verifyEmailAuth(token, "12345"));

        assertEquals(applicationException.getErrorCode(), ExceptionList.USER_AUTH_FAIL.getCODE());
    }

    @DisplayName("이미 가입된 유저인지 확인한다")
    @Test
    void checkUser() {
        // given
        userRepository.saveAndFlush(createUser("test@gmail.com", "12345"));

        // when
        Boolean response = userService.checkUser("test@gmail.com");

        // then
        assertThat(response).isTrue();
    }

    @DisplayName("인증된 유저의 프로필 확인")
    @Test
    void getUserProfile() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());

        // when
        var response = userService.getUserProfile(user);

        // then
        assertEquals(response.nickName(), request.nickname());
    }

    @DisplayName("인증된 사용자의 프로필 정보 수정")
    @Test
    void updateUserProfile() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());

        UserProfileUpdateReq userProfileUpdateReq = new UserProfileUpdateReq("연유크림", "imageKey2");

        // when
        var response = userService.updateUserProfile(user, userProfileUpdateReq);

        var updatedUser = userService.getUserProfile(user);

        // then
        assertTrue(response);
        assertEquals(updatedUser.nickName(), userProfileUpdateReq.nickName());
        assertEquals(
                updatedUser.profileImgUrl(),
                s3Util.getImgUrl(userProfileUpdateReq.profileImageKey()));
    }

    @DisplayName("인증된 사용자의 프로필 정보 수정 - 닉네임 중복")
    @Test
    void updateUserProfile_duplicated() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("nickname", token, "12345", university.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        userRepository.saveAndFlush(createUser("hello@world.com", "연유크림"));

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());

        UserProfileUpdateReq userProfileUpdateReq = new UserProfileUpdateReq("연유크림", "imageKey2");

        // when then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> userService.updateUserProfile(user, userProfileUpdateReq));

        assertEquals(
                applicationException.getErrorCode(),
                ExceptionList.NICKNAME_ALREADY_EXIST.getCODE());
    }

    @DisplayName("회원 탈퇴 - 정해진 사유를 선택한 경우")
    @Test
    void withdrawal() {
        // given
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        University university =
                universityRepository.save(
                        University.builder().name("명지대학교").campusName("인문캠퍼스").build());
        UserEmailSingReq request =
                new UserEmailSingReq("연유크림", token, "12345", university.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());

        WithdrawalReq withdrawalReq =
                new WithdrawalReq(WithdrawalReason.ERRORS_OCCUR_FREQUENTLY, "");

        // when then
        var result = userService.withdrawal(user, withdrawalReq);
        var withdrawalUser = userRepository.findByNickname("연유크림").get();

        assertEquals(result, Boolean.TRUE);
        assertEquals(withdrawalUser.getIsDeleted(), Boolean.TRUE);
    }

    private User createUser(String email, String nickname) {
        return User.builder().email(email).nickname(nickname).build();
    }
}
