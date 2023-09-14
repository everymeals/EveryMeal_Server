package everymeal.server.global;


import com.fasterxml.jackson.databind.ObjectMapper;
import everymeal.server.meal.controller.MealController;
import everymeal.server.meal.service.MealService;
import everymeal.server.university.controller.UniversityController;
import everymeal.server.university.service.UniversityService;
import everymeal.server.user.controller.UserController;
import everymeal.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {UserController.class, MealController.class, UniversityController.class})
public abstract class ControllerTestSupport {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;

    @MockBean protected UserService userService;

    @MockBean protected MealService mealService;

    @MockBean protected UniversityService universityService;
}
