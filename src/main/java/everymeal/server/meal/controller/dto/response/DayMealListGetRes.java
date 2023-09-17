package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DayMealListGetRes {

    private String menu;
    private MealType mealType;
    private MealStatus mealStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate offeredAt;

    private Double price;

    public static List<DayMealListGetRes> of(List<Meal> mealList) {
        return mealList.stream()
                .map(
                        meal ->
                                DayMealListGetRes.builder()
                                        .menu(meal.getMenu())
                                        .mealType(meal.getMealType())
                                        .mealStatus(meal.getMealStatus())
                                        .offeredAt(meal.getOfferedAt())
                                        .price(meal.getPrice())
                                        .build())
                .toList();
    }
}
