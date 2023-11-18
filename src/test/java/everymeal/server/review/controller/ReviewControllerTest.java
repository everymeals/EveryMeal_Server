package everymeal.server.review.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class ReviewControllerTest extends ControllerTestSupport {
    private String ACCESS_TOKEN = "accessToken";

    @BeforeEach
    void 로그인() {
        UserEmailSingReq singReq =
                new UserEmailSingReq("testNickname", "Token", "sendValue", 1L, "testImageKey");

        given(userService.signUp(any()))
                .willReturn(
                        new UserLoginRes(
                                "accessToken", "testNickname", "testImageKey", "refreshToken"));

        given(userService.login(any()))
                .willReturn(
                        new UserLoginRes(
                                "accessToken", "testNickname", "testImageKey", "refreshToken"));
    }

    @DisplayName("리뷰 등록")
    @Test
    void createReview() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 5, "오늘 학식 진짜 미침", List.of());

        // when-then
        mockMvc.perform(
                        post("/api/v1/reviews")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, ACCESS_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 수정")
    @Test
    void updateReview() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 5, "오늘 학식 진짜 미침", List.of());
        Long reviewIdx = 1L;
        // when-then
        mockMvc.perform(
                        put("/api/v1/reviews/{reviewIdx}", reviewIdx)
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, ACCESS_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 삭제")
    @Test
    void deleteReview() throws Exception {
        // given
        Long reviewIdx = 1L;
        // when-then
        mockMvc.perform(
                        delete("/api/v1/reviews/{reviewIdx}", reviewIdx)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, ACCESS_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 커서 기반 페이징 조회")
    @Test
    void getReviewWithNoOffSetPaging() throws Exception {
        // given
        Long cursorIdx = 1L;
        Long mealIdx = 1L;
        int pageSize = 8;
        // when-then
        mockMvc.perform(
                        get("/api/v1/reviews?cursorIdx="
                                        + cursorIdx
                                        + "&mealIdx="
                                        + mealIdx
                                        + "&pageSize="
                                        + pageSize)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
