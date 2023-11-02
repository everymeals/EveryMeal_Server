package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.util.List;

public record WeekMealListGetRes(
        @JsonFormat(
                        shape = JsonFormat.Shape.STRING,
                        pattern = "yyyy-MM-dd",
                        timezone = "Asia/Seoul")
                LocalDate offeredAt,
        List<DayMealListGetRes> dayMealListGetResListTest) {
    @QueryProjection
    public WeekMealListGetRes(
            LocalDate offeredAt, List<DayMealListGetRes> dayMealListGetResListTest) {
        this.offeredAt = offeredAt;
        this.dayMealListGetResListTest = dayMealListGetResListTest;
    }
}
