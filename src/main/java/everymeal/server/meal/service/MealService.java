package everymeal.server.meal.service;


import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import java.util.List;
import java.util.Map;

public interface MealService {

    Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq);

    Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq);

    List<RestaurantListGetRes> getRestaurantList(String universityName, String campusName);

    Map<String, List<DayMealListGetRes>> getDayMealList(
            String universityName, String campusName, String offeredAt);

    List<WeekMealListGetRes> getWeekMealListTest(String universityName, String offeredAt);
}
