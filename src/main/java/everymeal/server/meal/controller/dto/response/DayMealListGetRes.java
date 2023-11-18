package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import java.time.LocalDate;

public record DayMealListGetRes(
        Long mealIdx,
        @JsonFormat(
                        shape = JsonFormat.Shape.STRING,
                        pattern = "yyyy-MM-dd",
                        timezone = "Asia/Seoul")
                LocalDate offeredAt,
        MealStatus mealStatus,
        MealType mealType,
        String menu,
        Double price,
        MealCategory category,
        String restaurantName,
        String universityName) {}
