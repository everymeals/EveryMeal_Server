package everymeal.server.store.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.global.util.authresolver.UserJwtResolver;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class StoreControllerTest extends ControllerTestSupport {

    @Mock UserJwtResolver userJwtResolver;

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
                        get("/api/v1/stores/{campusIdx}", campusIdx)
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
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group))
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
                        get("/api/v1/stores/{campusIdx}", campusIdx)
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
                        get("/api/v1/stores/{campusIdx}", campusIdx)
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
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("order", order)
                                .param("group", group))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
