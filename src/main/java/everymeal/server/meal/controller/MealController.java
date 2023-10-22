package everymeal.server.meal.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 학생식당 등록 API
     *
     * @param restaurantRegisterReq RestaurantRegisterReq 식당 정보 입력
     * @return restID String 학생식당 고유 번호
     * @author dldmsql
     */
    @PostMapping("/restaurant")
    @Operation(summary = "학생식당 추가")
    public ApplicationResponse<Boolean> createRestaurant(
            @RequestBody @Valid RestaurantRegisterReq restaurantRegisterReq) {
        return ApplicationResponse.create(mealService.createRestaurant(restaurantRegisterReq));
    }

    /**
     * 학생식당 조회 API
     *
     * @param universityName String 학교 이름
     * @param campusName String 캠퍼스 이름
     * @return List<RestaurantListGetRes> 학교+캠퍼스로 등록된 식당 리스트
     * @author dldmsql
     */
    @GetMapping("/restaurant")
    @Operation(summary = "학교별 학생 식당 목록 조회")
    public ApplicationResponse<List<RestaurantListGetRes>> getRestaurants(
            @Schema(title = "대학 이름", defaultValue = "명지대학교", example = "명지대학교")
                    @RequestParam(value = "universityName")
                    String universityName,
            @Schema(title = "캠퍼스 이름", defaultValue = "인문캠퍼스", example = "인문캠퍼스")
                    @RequestParam(value = "campusName")
                    String campusName) {
        return ApplicationResponse.ok(mealService.getRestaurantList(universityName, campusName));
    }

    /**
     * 주간 단위 식단 등록 API
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
     * 하루 식단 조회 API
     *
     * @param restaurantIdx Long
     * @return
     * @author dldmsql
     */
    @GetMapping("/day")
    @Operation(summary = "당일 식단 조회")
    public ApplicationResponse<List<DayMealListGetRes>> getDayMeal(
            @RequestParam @Schema(description = "학생식당 PK", defaultValue = "1") Long restaurantIdx,
            @RequestParam
                    @Schema(description = "조회하고자 하는 날짜 ( yyyy-MM-dd )", defaultValue = "2023-10-01")
                    String offeredAt) {
        return ApplicationResponse.ok(mealService.getDayMealList(restaurantIdx, offeredAt));
    }

    /**
     * 주간 단위 식단 조회 API
     *
     * @param restaurantIdx 식당 아이디
     * @param offeredAt 조회날짜
     * @author dldmsql
     */
    @GetMapping("/week")
    @Operation(summary = "주간 식단 조회")
    public ApplicationResponse<List<WeekMealListGetRes>> getWeekMeal(
            @RequestParam @Schema(description = "학생식당 PK", defaultValue = "1") Long restaurantIdx,
            @RequestParam
                    @Schema(
                            description = "조회하고자 하는 시작 날짜 ( yyyy-MM-dd )",
                            defaultValue = "2023-10-01")
                    String offeredAt) {
        return ApplicationResponse.ok(mealService.getWeekMealListTest(restaurantIdx, offeredAt));
    }
}
