package everymeal.server.meal.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meals")
@Tag(name = "Meal API", description = "학식 식단 관련 API입니다")
public class MealController {

    private final MealService mealService;

    /**
     * 학생식당 등록 API
     *
     * @param restaurantRegisterReq RestaurantRegisterReq 식당 정보 입력
     * @return restID String 학생식당 고유 번호
     * @author dldmsql
     */
    @PostMapping("/restaurant")
    @Operation(summary = "학생식당 추가")
    public ApplicationResponse<String> createRestaurant(
            @RequestBody @Valid RestaurantRegisterReq restaurantRegisterReq) {
        return ApplicationResponse.create(mealService.createRestaurant(restaurantRegisterReq));
    }

    /**
     * 주간단위 식단 등록 API
     *
     * @param weekMealRegisterReq WeekMealRegisterReq 식당 정보 입력
     * @return boolean 성공
     * @author dldmsql
     */
    @PostMapping("/week")
    @Operation(summary = "주간 단위 식단 추가")
    public ApplicationResponse<Boolean> createWeekMeal(
            @RequestBody @Valid WeekMealRegisterReq weekMealRegisterReq) {
        return ApplicationResponse.create(mealService.createWeekMeal(weekMealRegisterReq));
    }

    /**
     * 당일 식단 조회 API
     *
     * @param restaurantId String
     * @return
     * @author dldmsql
     */
    @GetMapping("/{restaurantId}")
    @Operation(summary = "당일 식단 조회")
    public ApplicationResponse<List<Meal>> getDayMeal(
            @PathVariable @Schema(description = "학생식당 PK", defaultValue = "restaurantId")
                    String restaurantId) {
        return ApplicationResponse.ok(mealService.getDayMeal(restaurantId));
    }
}
