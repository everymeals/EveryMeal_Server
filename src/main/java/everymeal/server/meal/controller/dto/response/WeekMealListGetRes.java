package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WeekMealListGetRes {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate offeredAt;

    private List<DayMealListGetRes> dayMealListGetResListTest;

    @QueryProjection
    public WeekMealListGetRes(
            LocalDate offeredAt, List<DayMealListGetRes> dayMealListGetResListTest) {
        this.offeredAt = offeredAt;
        this.dayMealListGetResListTest = dayMealListGetResListTest;
    }
}
