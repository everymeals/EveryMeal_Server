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

    Map<String, Map<String, List<DayMealGetRes>>> getDayMealList(
            Long universityIdx, String offeredAt);

    List<Map<String, Map<String, List<DayMealGetRes>>>> getWeekMealList(
            Long universityIdx, String offeredAt);

    Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq);
}
