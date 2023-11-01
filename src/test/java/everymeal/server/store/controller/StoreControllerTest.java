package everymeal.server.store.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class StoreControllerTest extends ControllerTestSupport {

    @DisplayName("가게 목록을 거리순으로 조회한다.")
    @Test
    void getStores() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String orderBy = "distance";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("orderBy", orderBy))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 이름순으로 조회한다.")
    @Test
    void getStoresOrderByName() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String orderBy = "name";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("orderBy", orderBy))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 평점순으로 조회한다.")
    @Test
    void getStoresOrderByGrade() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String orderBy = "grade";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("orderBy", orderBy))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 리뷰 순으로 조회한다.")
    @Test
    void getStoresOrderByreviewCount() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String orderBy = "reviewCount";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("orderBy", orderBy))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("가게 목록을 추천 순으로 조회한다.")
    @Test
    void getStoresOrderByRecommendCount() throws Exception {
        // given
        Long campusIdx = 1L;
        int offset = 0;
        int limit = 10;
        String orderBy = "reviewCount";

        // when then
        mockMvc.perform(
                        get("/api/v1/stores/{campusIdx}", campusIdx)
                                .param("offset", String.valueOf(offset))
                                .param("limit", String.valueOf(limit))
                                .param("orderBy", orderBy))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
