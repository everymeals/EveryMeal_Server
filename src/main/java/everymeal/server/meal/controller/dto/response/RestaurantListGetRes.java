package everymeal.server.meal.controller.dto.response;


import everymeal.server.meal.entity.Restaurant;
import java.util.List;

public record RestaurantListGetRes(
        String universityName,
        String campusName,
        String restaurantName,
        String address,
        Long restaurantIdx) {
    public static List<RestaurantListGetRes> of(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(
                        restaurant ->
                                new RestaurantListGetRes(
                                        restaurant.getUniversity().getName(),
                                        restaurant.getUniversity().getCampusName(),
                                        restaurant.getName(),
                                        restaurant.getAddress(),
                                        restaurant.getIdx()))
                .toList();
    }
}
