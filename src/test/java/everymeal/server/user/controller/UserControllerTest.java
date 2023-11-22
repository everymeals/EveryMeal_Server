package everymeal.server.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("회원가입을 진행한다.")
    @Test
    void signUp() throws Exception {
        // given
        UserEmailSingReq request =
                new UserEmailSingReq("testNickname", "Token", "sendValue", 1L, "testImageKey");

        given(userService.signUp(any()))
                .willReturn(
                        new UserLoginRes(
                                "accessToken", "testNickname", "testImageKey", "refreshToken"));

        // when then
        mockMvc.perform(
                        post("/api/v1/users/signup")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(cookie().exists("refresh-token"));
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() throws Exception {
        // given
        UserEmailLoginReq request = new UserEmailLoginReq("testJwtToken", "145734");

        given(userService.login(any()))
                .willReturn(
                        new UserLoginRes(
                                "accessToken", "testNickname", "testImageKey", "refreshToken"));

        // when then
        mockMvc.perform(
                        post("/api/v1/users/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(cookie().exists("refresh-token"));
    }

    @DisplayName("이메일 전송합니다.")
    @Test
    void emailAuth() throws Exception {
        // given
        UserEmailAuthReq request = new UserEmailAuthReq("test@gmail.com");

        // when then
        mockMvc.perform(
                        post("/api/v1/users/email")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("이메일 인증을 진행합니다.")
    @Test
    void verifyEmailAuth() throws Exception {
        // given

        String emailAuthToken = "testToken";
        String emailAuthValue = "testValue";

        // when then
        mockMvc.perform(
                        get("/api/v1/users/email/verify")
                                .param("emailAuthToken", emailAuthToken)
                                .param("emailAuthValue", emailAuthValue)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("이미 가입된 유저인지 확인")
    @Test
    void checkUser() throws Exception {
        // given
        UserEmailAuthReq request = new UserEmailAuthReq("test@gmail.com");

        given(userService.checkUser(any())).willReturn(true);

        // when then
        mockMvc.perform(
                        get("/api/v1/users/email")
                                .queryParam("email", "test@gmail.com")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
