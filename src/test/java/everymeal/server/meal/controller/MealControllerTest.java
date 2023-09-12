package everymeal.server.meal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.University;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class MealControllerTest extends ControllerTestSupport {

    @DisplayName("주간 단위 식사 등록")
    @Test
    void createWeekMeal() throws Exception {
        // given
        WeekMealRegisterReq req = new WeekMealRegisterReq();
        req.setRestaurantId(UUID.randomUUID().toString());
        List<MealRegisterReq> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq = new MealRegisterReq();
            mealReq.setMealStatus(MealStatus.OPEN.name());
            mealReq.setMenu("갈비탕, 깍두기, 흰쌀밥");
            mealReq.setMealType(MealType.BREAKFAST.name());
            mealReq.setPrice(10000.0);
            mealReq.setOfferedAt(LocalDate.now());
            list.add(mealReq);
        }
        req.setRegisterReqList(list);

        // when-then
        mockMvc.perform(
                        post("/api/v1/meals/week")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @DisplayName("당일 식단 조회")
    @Test
    void getDayMeal() throws Exception {
        // given
        String restaurantId = UUID.randomUUID().toString();

        // when-then
        mockMvc.perform(get("/api/v1/meals/{restaurantId}", restaurantId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    private RestaurantRegisterReq getRestaurantRegisterReq() {
        RestaurantRegisterReq req = new RestaurantRegisterReq();
        req.setName("MCC 식당");
        req.setAddress("서울시 서대문구 남가좌동 거북골로 34");
        req.setUniversity(University.MYONGJI_S.toString());
        return req;
    }
}
