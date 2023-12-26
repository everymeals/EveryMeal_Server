package everymeal.server.meal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class MealControllerTest extends ControllerTestSupport {

    @DisplayName("주간 단위 식사 등록")
    @Test
    void createWeekMeal() throws Exception {
        // given
        List<MealRegisterReq> list = new ArrayList<>(); // 식사 데이터
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.BREAKFAST.name(),
                            MealStatus.OPEN.name(),
                            LocalDate.now(),
                            10000.0,
                            MealCategory.DEFAULT.name());
            list.add(mealReq);
        }
        WeekMealRegisterReq req = new WeekMealRegisterReq(list, 1L);

        // when-then
        mockMvc.perform(
                        post("/api/v1/meals/week")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("학생 식당 추가")
    @Test
    void createRestaurant() throws Exception {
        // given
        RestaurantRegisterReq req = getRestaurantRegisterReq();

        // when-then
        mockMvc.perform(
                        post("/api/v1/meals/restaurant")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("학교별 학생 식당 목록 조회")
    @Test
    void getRestaurants() throws Exception {
        // given
        String universityName = "명지대학교";
        String campusName = "인문캠퍼스";

        // when-then
        mockMvc.perform(
                        get("/api/v1/meals/restaurant")
                                .param("universityName", universityName)
                                .param("campusName", campusName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("당일 식단 조회")
    @Test
    void getDayMeal() throws Exception {
        // given
        String universityName = "명지대학교";
        String campusName = "인문캠퍼스";
        String offeredAt = "2023-10-01";

        // when-then
        mockMvc.perform(
                        get("/api/v1/meals/day/v2")
                                .param("universityName", universityName)
                                .param("campusName", campusName)
                                .param("offeredAt", offeredAt)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("주간 식단 조회")
    @Test
    void getWeekMeal() throws Exception {
        // given
        String universityName = "명지대학교";
        String offeredAt = "2023-10-01";

        // when-then
        mockMvc.perform(
                        get("/api/v1/meals/week/v2")
                                .param("universityName", universityName)
                                .param("campusName", "인문캠퍼스")
                                .param("offeredAt", offeredAt)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    private RestaurantRegisterReq getRestaurantRegisterReq() {
        return new RestaurantRegisterReq(
                "명지대학교",
                "인문캠퍼스",
                "서울시 서대문구 남가좌동 거북골로 34",
                "MCC 식당",
                LocalTime.of(8, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(14, 30),
                LocalTime.of(17, 0),
                LocalTime.of(18, 30));
    }
}
