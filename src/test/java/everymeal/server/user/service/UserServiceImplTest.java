package everymeal.server.user.service;

import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceImplTest extends IntegrationTestSupport {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

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
}
