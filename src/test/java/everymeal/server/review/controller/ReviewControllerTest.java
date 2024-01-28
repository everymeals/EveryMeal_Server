package everymeal.server.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.global.util.authresolver.UserJwtResolver;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.review.dto.request.ReviewCreateReq;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class ReviewControllerTest extends ControllerTestSupport {

    @MockBean UserJwtResolver userJwtResolver;

    @DisplayName("리뷰 등록 - 오늘 먹은 거")
    @Test
    void createReview() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 5, "오늘 학식 진짜 미침", List.of(), true);

        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());

        // when-then
        mockMvc.perform(
                        post("/api/v1/reviews")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 등록")
    @Test
    void createReview_not_today() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 5, "오늘 학식 진짜 미침", List.of(), false);
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        post("/api/v1/reviews")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("사진 리뷰 등록 - 오늘 먹은 거")
    @Test
    void createReview_photo_today() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 0, null, List.of("img1", "img2"), true);
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        post("/api/v1/reviews")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("사진 리뷰 등록")
    @Test
    void createReview_photo_not_today() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 0, null, List.of("img1", "img2"), false);
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        post("/api/v1/reviews")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 수정")
    @Test
    void updateReview() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 4, "오늘 학식 진짜 미침", List.of(), true);
        Long reviewIdx = 1L;
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        put("/api/v1/reviews/{reviewIdx}", reviewIdx)
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 삭제")
    @Test
    void deleteReview() throws Exception {
        // given
        Long reviewIdx = 1L;
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        delete("/api/v1/reviews/{reviewIdx}", reviewIdx)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 커서 기반 페이징 조회")
    @Test
    void getReviewWithNoOffSetPaging() throws Exception {
        // given
        Long cursorIdx = 1L;
        Long restaurantIdx = 1L;
        int pageSize = 8;
        // when-then
        mockMvc.perform(
                        get("/api/v1/reviews?cursorIdx="
                                        + cursorIdx
                                        + "&restaurantIdx="
                                        + restaurantIdx
                                        + "&pageSize="
                                        + pageSize)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 좋아요")
    @Test
    void markReview() throws Exception {
        // given
        Long reviewIdx = 1L;
        Boolean isLike = true;
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        post(
                                        "/api/v1/reviews/mark?reviewIdx={reviewIdx}&isLike={isLike}",
                                        reviewIdx,
                                        isLike)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("오늘 먹었어요. 리뷰 조회")
    @Test
    void getTodayReview() throws Exception {
        // given
        Long restaurantIdx = 1L;
        String offeredAt = "2023-12-25";

        // when-then
        mockMvc.perform(
                        get("/api/v1/reviews/today?restaurantIdx="
                                        + restaurantIdx
                                        + "&offeredAt="
                                        + offeredAt)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("주변 식당 리뷰 생성")
    @Test
    void createReviewAround() throws Exception {
        // given
        ReviewCreateReq req = new ReviewCreateReq(1L, 5, "오늘 학식 진짜 미침", List.of(), true);
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());
        // when-then
        mockMvc.perform(
                        post("/api/v1/reviews/store")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
