package everymeal.server.meal.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import java.time.Instant;
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
public class DayMealListGetResTest {

    private String menu;
    private MealType mealType;
    private MealStatus mealStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Instant offeredAt;

    private Double price;
    private MealCategory category;
    private String restaurantName;


}
