package everymeal.server.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceImplTest extends IntegrationTestSupport {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private MailUtil mailUtil;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입을 진행한다.")
    @Test
    void singUp() {
        // given
        UserSingReq request = UserSingReq.builder().deviceId("123456789").build();

        // when
        Boolean response = userService.signUp(request);

        // then
        assertEquals(
                userRepository.findByDeviceId(request.getDeviceId()).get().getDeviceId(),
                request.getDeviceId());
        assertTrue(response);
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() {
        // given
        UserSingReq request = UserSingReq.builder().deviceId("123456789").build();
        userService.signUp(request);

        // when
        UserLoginRes response = userService.login(request);

        // then
        assertNotNull(response.getRefreshToken());
    }

    @DisplayName("유저 인증 여부를 반환한다.")
    @Test
    void isUserAuthenticated() {
        // given
        User user =
                userRepository.save(
                        User.builder()
                                .deviceId("dsafkml-fgsmkgrlms-421m4f")
                                .email("test@mju.ac.kr")
                                .build());
        AuthenticatedUser authenticatedUser =
                AuthenticatedUser.builder().deviceId("dsafkml-fgsmkgrlms-421m4f").build();

        // when
        Boolean response = userService.isAuth(authenticatedUser);

        // then
        assertTrue(response);
    }

    @DisplayName("이메일 인증을 진행한다.")
    @Test
    void emailAuth() {
        // given
        UserEmailAuthReq request = UserEmailAuthReq.builder().email("test@gmail.com").build();

        AuthenticatedUser authenticatedUser =
                AuthenticatedUser.builder().deviceId("dsafkml-fgsmkgrlms-421m4f").build();

        // when
        UserEmailAuthRes response = userService.emailAuth(request, authenticatedUser);

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("이메일 인증 확인을 진행한다.")
    @Test
    void emailAuthVerify() {
        // given
        User user = User.builder().deviceId("dsafkml-fgsmkgrlms-421m4f").email(null).build();
        userRepository.save(user);

        String token = jwtUtil.generateEmailToken(user.getIdx(), "test@gmail.com", "123456");
        UserEmailAuthVerifyReq request =
                UserEmailAuthVerifyReq.builder()
                        .emailAuthToken(token)
                        .emailAuthValue("123456")
                        .build();

        AuthenticatedUser authenticatedUser =
                AuthenticatedUser.builder()
                        .idx(user.getIdx())
                        .deviceId("dsafkml-fgsmkgrlms-421m4f")
                        .build();

        // when
        Boolean response = userService.verifyEmailAuth(request, authenticatedUser);

        // then
        assertTrue(response);
    }

    @DisplayName("이메일 인증 과정에서 없는 유저의 인덱스가 들어 있을 경우, 예외가 발생한다.")
    @Test
    void emailAuthVerifyWithNotExistUserIdx() {
        // given

        User user = User.builder().deviceId("dsafkml-fgsmkgrlms-421m4f").email(null).build();
        userRepository.save(user);

        String token = jwtUtil.generateEmailToken(user.getIdx(), "test@gmail.com", "123456");
        UserEmailAuthVerifyReq request =
                UserEmailAuthVerifyReq.builder()
                        .emailAuthToken(token)
                        .emailAuthValue("123456")
                        .build();

        AuthenticatedUser authenticatedUser =
                AuthenticatedUser.builder()
                        .idx(user.getIdx())
                        .deviceId("dsafkml-fgsmkgrlms-421m4f")
                        .build();

        userRepository.delete(user);

        // when & then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> userService.verifyEmailAuth(request, authenticatedUser));
        assertEquals(applicationException.getErrorCode(), ExceptionList.USER_NOT_FOUND.getCODE());
    }

    @DisplayName("토큰에서 추출한 인증값과 유저가 입력한 인증값이 일치하지 않을 경우, 예외가 발생한다.")
    @Test
    void emailAuthVerifyWithNotMatchAuthValue() {
        // given
        User user = User.builder().deviceId("dsafkml-fgsmkgrlms-421m4f").email(null).build();
        userRepository.save(user);

        String token = jwtUtil.generateEmailToken(user.getIdx(), "", "123456");

        UserEmailAuthVerifyReq request =
                UserEmailAuthVerifyReq.builder()
                        .emailAuthToken(token)
                        .emailAuthValue("1234567")
                        .build();

        AuthenticatedUser authenticatedUser =
                AuthenticatedUser.builder()
                        .idx(user.getIdx())
                        .deviceId("dsafkml-fgsmkgrlms-421m4f")
                        .build();

        // when & then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> userService.verifyEmailAuth(request, authenticatedUser));
        assertEquals(applicationException.getErrorCode(), ExceptionList.USER_AUTH_FAIL.getCODE());
    }
}
