package everymeal.server.infra;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HealthCheckControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("health-check")
    void healthCheck() throws Exception {
        // given
        String hello = "Server is Up!";
        // when

        // then
        mockMvc.perform(get("/health-check"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }
}
