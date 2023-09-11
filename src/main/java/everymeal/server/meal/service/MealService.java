package everymeal.server.meal.service;


import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.entity.Meal;
import java.util.List;

public interface MealService {

    String createRestaurant(RestaurantRegisterReq restaurantRegisterReq);

    Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq);

    List<Meal> getDayMeal(String restaurant);
}
