package everymeal.server.meal.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WeekMealListGetResTest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Instant offeredAt;

    private List<DayMealListGetResTest> dayMealListGetResListTest;

    @QueryProjection
    public WeekMealListGetResTest(Instant offeredAt,
        List<DayMealListGetResTest> dayMealListGetResListTest) {
        this.offeredAt = offeredAt;
        this.dayMealListGetResListTest = dayMealListGetResListTest;
    }
}
