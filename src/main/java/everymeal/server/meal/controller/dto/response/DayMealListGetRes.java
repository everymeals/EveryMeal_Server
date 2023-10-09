package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import everymeal.server.meal.entity.Meal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DayMealListGetRes {

    private String menu;
    private String mealType;
    private String mealStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Instant offeredAt;

    private Double price;
    private String category;
    private String restaurantName;

    public static List<DayMealListGetRes> of(List<Meal> mealList) {
        return mealList.stream()
                .map(
                        meal ->
                                DayMealListGetRes.builder()
                                        .menu(meal.getMenu())
                                        .mealType(meal.getMealType().getValue())
                                        .mealStatus(meal.getMealStatus().getValue())
                                        .offeredAt(meal.getOfferedAt().truncatedTo(ChronoUnit.DAYS))
                                        .price(meal.getPrice())
                                        .category(meal.getCategory().name())
                                        .restaurantName(meal.getRestaurant().getName())
                                        .build())
                .toList();
    }
}
