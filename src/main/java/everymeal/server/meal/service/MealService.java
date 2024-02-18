package everymeal.server.meal.service;


import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import java.util.List;
import java.util.Map;

public interface MealService {

    Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq);

    List<RestaurantListGetRes> getRestaurantList(Long universityIdx);

    Map<String, Map<String, List<DayMealGetRes>>> getDayMealListV2(
            String universityName, String campusName, String offeredAt);

    List<Map<String, Map<String, List<DayMealGetRes>>>> getWeekMealList(
            String universityName, String campusName, String offeredAt);

    Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq);
}
