package everymeal.server.meal.controller.dto.response;


import everymeal.server.meal.entity.Restaurant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RestaurantListGetRes {

    private String universityName;
    private String campusName;
    private String restaurantName;
    private String address;
    private Long restaurantIdx;

    public static List<RestaurantListGetRes> of(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(
                        restaurant ->
                                RestaurantListGetRes.builder()
                                        .universityName(restaurant.getUniversity().getName())
                                        .campusName(restaurant.getUniversity().getCampusName())
                                        .restaurantName(restaurant.getName())
                                        .address(restaurant.getAddress())
                                        .restaurantIdx(restaurant.getIdx())
                                        .build())
                .toList();
    }
}
