package everymeal.server.meal.service;


import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.entity.Restaurant;
import java.util.List;

public interface RestaurantService {

    Restaurant getRestaurant(Long restaurantIdx);

    Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq);

    List<Restaurant> getAllByUniversityAndIsDeletedFalse(Long universityIdx);
}
