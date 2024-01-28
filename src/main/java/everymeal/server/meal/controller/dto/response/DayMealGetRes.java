package everymeal.server.meal.controller.dto.response;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record DayMealGetRes(
        Long mealIdx,
        String offeredAt,
        String mealStatus,
        String mealType,
        String operatingTime,
        String menu,
        Double price,
        String category,
        Long restaurantIdx,
        String restaurantName,
        String universityName,
        Integer reviewCount,
        Double grade) {
    public static Map<String, List<DayMealGetRes>> of(List<Map<String, Object>> meals) {
        return meals.stream()
                .map(
                        meal ->
                                new DayMealGetRes(
                                        (Long) meal.get("mealIdx"),
                                        (String) meal.get("offeredAt"),
                                        (String) meal.get("mealStatus"),
                                        (String) meal.get("mealType"),
                                        (String) meal.get("operatingTime"),
                                        (String) meal.get("menu"),
                                        (Double) meal.get("price"),
                                        (String) meal.get("category"),
                                        (Long) meal.get("idx"),
                                        (String) meal.get("restaurantName"),
                                        (String) meal.get("universityName"),
                                        (Integer) meal.get("reviewCount"),
                                        (Double) meal.get("grade")))
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                DayMealGetRes::restaurantName,
                                TreeMap::new, // TreeMap 사용하여 오름차순 정렬
                                java.util.stream.Collectors.toList()));
    }
}
