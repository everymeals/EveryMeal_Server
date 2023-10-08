package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeekMealListGetRes {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Instant offeredAt;

    private List<DayMealListGetRes> dayMealListGetResList;

    public static List<WeekMealListGetRes> of(List<DayMealListGetRes> mealListGetRes) {
        // offeredAt을 기준으로 DayMealList 그룹핑
        Map<Instant, List<DayMealListGetRes>> groupedData =
                mealListGetRes.stream()
                        .collect(Collectors.groupingBy(DayMealListGetRes::getOfferedAt));
        List<WeekMealListGetRes> weekMealList =
                groupedData.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(
                                entry ->
                                        WeekMealListGetRes.builder()
                                                .offeredAt(entry.getKey())
                                                .dayMealListGetResList(entry.getValue())
                                                .build())
                        .collect(Collectors.toList());

        return weekMealList;
    }
}
