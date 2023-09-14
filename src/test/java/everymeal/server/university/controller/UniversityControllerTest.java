package everymeal.server.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class UniversityControllerTest extends ControllerTestSupport {

    @DisplayName("대학교 리스트를 조회한다.")
    @Test
    void getUniversityList() throws Exception {
        // given
        String universityName = "명지대학교";
        String campusName = "인문캠퍼스";

        // when then
        mockMvc.perform(
                        get("/api/v1/universities")
                                .param("universityName", universityName)
                                .param("campusName", campusName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
