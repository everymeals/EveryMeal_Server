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

    @DisplayName("가게 목록을 조회한다.")
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
}
