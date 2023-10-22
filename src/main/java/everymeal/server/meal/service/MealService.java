package everymeal.server.meal.service;


import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import java.util.List;

public interface MealService {

    Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq);

    Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq);

    List<RestaurantListGetRes> getRestaurantList(String universityName, String campusName);

    List<DayMealListGetRes> getDayMealList(Long restaurantIdx, String offeredAt);

    List<WeekMealListGetRes> getWeekMealListTest(Long restaurantIdx, String offeredAt);
}
