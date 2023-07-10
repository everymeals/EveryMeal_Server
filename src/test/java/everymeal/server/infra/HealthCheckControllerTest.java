package everymeal.server.infra;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.infra.HealthCheckController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest {

  @Autowired
  MockMvc mvc;

  @Test
  @DisplayName("health-check")
  void healthCheck() throws Exception {
    //given
    String hello = "Server is Up!";
    //when

    //then
    mvc.perform(get("/health-check"))
        .andExpect(status().isOk())
        .andExpect(content().string(hello));
  }
}