package everymeal.server.store.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.global.util.authresolver.UserJwtResolver;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class StoreControllerTest extends ControllerTestSupport {

    @MockBean UserJwtResolver userJwtResolver;

    @DisplayName("가게 목록을 거리순으로 조회한다.")
    @Test
    void getStores() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String order = "distance";
        String group = "etc";

        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/campus/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group)
                                .param("grade", "0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 이름순으로 조회한다.")
    @Test
    void getStoresOrderByName() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String order = "name";
        String group = "etc";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/campus/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group)
                                .param("grade", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 평점순으로 조회한다.")
    @Test
    void getStoresOrderByGrade() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String order = "grade";
        String group = "etc";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/campus/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 리뷰 순으로 조회한다.")
    @Test
    void getStoresOrderByreviewCount() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String order = "reviewCount";
        String group = "etc";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/campus/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 추천 순으로 조회한다.")
    @Test
    void getStoresOrderByRecommendCount() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String order = "recommendedCount";
        String group = "etc";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/campus/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("인증된 사용자의 저장 목록 조회")
    @Test
    void getUserLikesStore() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String group = "all";

        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/likes")
                                .param("campusIdx", String.valueOf(campusIdx))
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("group", group))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 저장 / 저장 해제")
    @Test
    void likesStore_like() throws Exception {
        // given
        Long storeIdx = 1L;

        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());

        // when then
        mockMvc.perform(post("/api/v1/stores/likes/{storeIdx}", storeIdx))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("주변 식당 키워드 검색")
    @Test
    void searchStores() throws Exception {
        // given
        Long campusIdx = 1L;
        String keyword = "테스트";
        int offset = 0;
        int limit = 10;

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/{campusIdx}/{keyword}", campusIdx, keyword)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 상세 조회")
    @Test
    void getStoreDetail() throws Exception {
        // given
        Long storeIdx = 1L;

        // when then
        mockMvc.perform(get("/api/v1/stores/{storeIdx}", storeIdx))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
