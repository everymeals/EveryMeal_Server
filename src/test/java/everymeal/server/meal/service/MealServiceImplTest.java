package everymeal.server.meal.service;


import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.UniversityEnum;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.MealRepositoryCustom;
import everymeal.server.meal.repository.RestaurantRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MealServiceImplTest extends IntegrationTestSupport {

    @Autowired private MealService mealService;

    @Autowired private MealRepository mealRepository;

    @Autowired private MealRepositoryCustom mealRepositoryCustom;

    @Autowired private RestaurantRepository restaurantRepository;

    @DisplayName("학생 식당을 등록합니다.")
    @Test
    @Order(1)
    void createRestaurant() throws Exception {
        // given
        RestaurantRegisterReq req = getRestaurantRegisterReq();

        // when
        String response = mealService.createRestaurant(req);

        // then
        //        assertEquals(restaurantRepository.findById(response).get().getName(),
        // req.getName());
    }

    @DisplayName("주간 식단을 등록합니다.")
    @Test
    @Order(2)
    void createWeekMeal() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        String restaurant = mealService.createRestaurant(restaurantRegisterReq);

        WeekMealRegisterReq req = new WeekMealRegisterReq();
        req.setRestaurantId(restaurant);
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

        // when
        Boolean response = mealService.createWeekMeal(req);

        // then
        //        assertTrue(response);
    }

    @DisplayName("당일 식단 조회")
    @Test
    @Order(3)
    void getDayMeal() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        String restaurant = mealService.createRestaurant(restaurantRegisterReq);

        WeekMealRegisterReq req = new WeekMealRegisterReq();
        req.setRestaurantId(restaurant);
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

        // when
        Boolean isCreated = mealService.createWeekMeal(req);

        // when
        List<Meal> response = mealService.getDayMeal(restaurant);

        // then
        //        assertEquals(response.get(0).getRestaurant().get_id(), restaurant);
    }

    private RestaurantRegisterReq getRestaurantRegisterReq() {
        RestaurantRegisterReq req = new RestaurantRegisterReq();
        req.setName("MCC 식당");
        req.setAddress("서울시 서대문구 남가좌동 거북골로 34");
        req.setUniversity(UniversityEnum.MYONGJI_S.toString());
        return req;
    }
}
