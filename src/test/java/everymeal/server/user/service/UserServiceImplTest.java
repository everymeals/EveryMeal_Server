package everymeal.server.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.MailUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailAuthVerifyReq;
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
        String deviceId = "123456789";

        // when
        Boolean response = userService.signUp(deviceId);

        // then
        assertEquals(userRepository.findByDeviceId(deviceId).get().getDeviceId(), deviceId);
        assertTrue(response);
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() {
        // given
        String deviceId = "123456789";
        userService.signUp(deviceId);

        // when
        UserLoginRes response = userService.login(deviceId);

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
}
