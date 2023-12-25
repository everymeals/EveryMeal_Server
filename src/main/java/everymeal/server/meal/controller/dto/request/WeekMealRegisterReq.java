package everymeal.server.meal.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record WeekMealRegisterReq(
        @Schema(description = "등록하고자 하는 식단 데이터 객체") List<MealRegisterReq> registerReqList,
        @Schema(description = "학생식당 PK", defaultValue = "1") Long restaurantIdx) {}
