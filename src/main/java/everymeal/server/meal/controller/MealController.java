package everymeal.server.meal.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meals")
@Tag(name = "Meal API", description = "학식 식단 관련 API입니다")
public class MealController {

    private final MealService mealService;

    /**
     * ============================================================================================
     * 학생식당 등록 API
     * ============================================================================================
     */
    @PostMapping("/restaurant")
    @Operation(summary = "학생식당 추가")
    public ApplicationResponse<Boolean> createRestaurant(
            @RequestBody @Valid RestaurantRegisterReq restaurantRegisterReq) {
        return ApplicationResponse.create(mealService.createRestaurant(restaurantRegisterReq));
    }

    /**
     * ============================================================================================
     * 학생식당 조회 API
     * ============================================================================================
     */
    @GetMapping("/restaurant/{universityIdx}")
    @Operation(summary = "학교별 학생 식당 목록 조회")
    public ApplicationResponse<List<RestaurantListGetRes>> getRestaurants(
            @Schema(title = "대학 캠퍼스 IDX", defaultValue = "1", example = "1")
                    @PathVariable(value = "universityIdx")
                    Long universityIdx) {
        return ApplicationResponse.ok(mealService.getRestaurantList(universityIdx));
    }

    /**
     * ============================================================================================
     * 주간 단위 식단 등록 API
     * ============================================================================================
     */
    @PostMapping("/week")
    @Operation(summary = "주간 단위 식단 추가")
    public ApplicationResponse<Boolean> createWeekMeal(
            @RequestBody @Valid WeekMealRegisterReq weekMealRegisterReq) {
        return ApplicationResponse.create(mealService.createWeekMeal(weekMealRegisterReq));
    }

    @GetMapping("/day/v2")
    @Operation(summary = "당일 식단 조회")
    public ApplicationResponse<Map<String, Map<String, List<DayMealGetRes>>>> getDayMealV2(
            @RequestParam @Schema(description = "학교 이름", defaultValue = "명지대학교")
                    String universityName,
            @RequestParam @Schema(description = "캠퍼스 이름", defaultValue = "인문캠퍼스") String campusName,
            @RequestParam
                    @Schema(description = "조회하고자 하는 날짜 ( yyyy-MM-dd )", defaultValue = "2023-10-01")
                    String offeredAt) {
        return ApplicationResponse.ok(
                mealService.getDayMealListV2(universityName, campusName, offeredAt));
    }

    @GetMapping("/week/v2")
    @Operation(summary = "주간 식단 조회")
    public ApplicationResponse<List<Map<String, Map<String, List<DayMealGetRes>>>>> getWeekMealV2(
            @RequestParam @Schema(description = "학교 이름", defaultValue = "명지대학교")
                    String universityName,
            @RequestParam @Schema(description = "캠퍼스 이름", defaultValue = "인문캠퍼스") String campusName,
            @RequestParam
                    @Schema(
                            description = "조회하고자 하는 시작 날짜 ( yyyy-MM-dd )",
                            defaultValue = "2023-10-01")
                    String offeredAt) {
        return ApplicationResponse.ok(
                mealService.getWeekMealList(universityName, campusName, offeredAt));
    }
}
