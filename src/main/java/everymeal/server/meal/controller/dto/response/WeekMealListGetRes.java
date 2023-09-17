package everymeal.server.meal.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    private LocalDate offeredAt;

    private List<DayMealListGetRes> dayMealListGetResList;

    public static List<WeekMealListGetRes> of(List<DayMealListGetRes> mealListGetRes) {
        // offeredAt을 기준으로 DayMealList 그룹핑
        Map<LocalDate, List<DayMealListGetRes>> groupedData =
                mealListGetRes.stream()
                        .collect(Collectors.groupingBy(DayMealListGetRes::getOfferedAt));
        List<WeekMealListGetRes> weekMealList = new ArrayList<>();
        for (Map.Entry<LocalDate, List<DayMealListGetRes>> entry : groupedData.entrySet()) {
            WeekMealListGetRes weekMeal =
                    WeekMealListGetRes.builder()
                            .offeredAt(entry.getKey())
                            .dayMealListGetResList(entry.getValue())
                            .build();
            weekMealList.add(weekMeal);
        }
        // offeredAt을 기준으로 오름차순 정렬
        weekMealList.sort(Comparator.comparing(WeekMealListGetRes::getOfferedAt));
        return weekMealList;
    }
}
