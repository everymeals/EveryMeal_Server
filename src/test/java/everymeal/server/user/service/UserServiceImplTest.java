package everymeal.server.user.service;

import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
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
}
