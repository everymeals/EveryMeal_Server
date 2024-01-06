package everymeal.server.meal;


import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.university.entity.University;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class MealData {

    public static MealRegisterReq getMealRegisterReq(String menu) {
        return new MealRegisterReq(menu, "LUNCH", "OPEN", LocalDate.now(), 0.0, "category");
    }

    public static WeekMealRegisterReq getWeekMealRegisterReq() {
        return new WeekMealRegisterReq(
                List.of(
                        getMealRegisterReq("menu1"),
                        getMealRegisterReq("menu2"),
                        getMealRegisterReq("menu3"),
                        getMealRegisterReq("menu4"),
                        getMealRegisterReq("menu5"),
                        getMealRegisterReq("menu6"),
                        getMealRegisterReq("menu7")),
                1L);
    }

    public static WeekMealRegisterReq getWeekMealRegisterReq(int i, Long idx) {
        return new WeekMealRegisterReq(
                List.of(
                        getMealRegisterReq(i),
                        getMealRegisterReq(i + 1),
                        getMealRegisterReq(i + 2),
                        getMealRegisterReq(i + 3),
                        getMealRegisterReq(i + 4),
                        getMealRegisterReq(i + 5),
                        getMealRegisterReq(i + 6)),
                idx);
    }

    public static MealRegisterReq getMealRegisterReq() {
        return new MealRegisterReq(
                "menu",
                MealType.LUNCH.name(),
                MealStatus.OPEN.name(),
                LocalDate.now(),
                0.0,
                MealCategory.DEFAULT.name());
    }

    public static MealRegisterReq getMealRegisterReq(int i) {
        return new MealRegisterReq(
                "갈비탕, 깍두기, 흰쌀밥",
                MealType.LUNCH.name(),
                MealStatus.OPEN.name(),
                LocalDate.now().plusDays(i),
                10000.0,
                MealCategory.DEFAULT.name());
    }

    public static RestaurantRegisterReq getRestaurantRegisterReq() {
        return new RestaurantRegisterReq(
                "명지대학교",
                "인문캠퍼스",
                "서울시 서대문구 거북골로 34",
                "MCC 식당",
                LocalTime.of(8, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(14, 30),
                LocalTime.of(17, 0),
                LocalTime.of(18, 30));
    }

    public static University getUniversity() {
        return University.builder().name("명지대학교").campusName("인문캠퍼스").build();
    }

    public static University getUniversity(String name, String campusName) {
        return University.builder().name(name).campusName(campusName).build();
    }

    public static Restaurant getRestaurant() {
        return Restaurant.builder()
                .restaurantRegisterReq(getRestaurantRegisterReq())
                .university(getUniversity())
                .build();
    }

    public static Restaurant getRestaurant(
            University university, RestaurantRegisterReq restaurantRegisterReq) {
        return Restaurant.builder()
                .restaurantRegisterReq(restaurantRegisterReq)
                .university(university)
                .build();
    }

    public static Map<String, Map<String, List<DayMealGetRes>>> getDayMealList() {
        return Map.of(
                "2021-10-01",
                Map.of(
                        "BREAKFAST",
                        List.of(
                                new DayMealGetRes(
                                        1L,
                                        "2021-10-01",
                                        "OPEN",
                                        "BREAKFAST",
                                        "08:00 ~ 10:30",
                                        "갈비탕, 깍두기, 흰쌀밥",
                                        10000.0,
                                        "DEFAULT",
                                        1L,
                                        "MCC 식당",
                                        "명지대학교",
                                        0,
                                        0.0))));
    }

    public static List<Map<String, Map<String, List<DayMealGetRes>>>> getWeekMealList() {
        return List.of(
                Map.of(
                        "2021-10-01",
                        Map.of(
                                "BREAKFAST",
                                List.of(
                                        new DayMealGetRes(
                                                1L,
                                                "2021-10-01",
                                                "OPEN",
                                                "BREAKFAST",
                                                "08:00 ~ 10:30",
                                                "갈비탕, 깍두기, 흰쌀밥",
                                                10000.0,
                                                "DEFAULT",
                                                1L,
                                                "MCC 식당",
                                                "명지대학교",
                                                0,
                                                0.0)))));
    }

    public static Meal getMealEntity(Restaurant restaurant) {
        return Meal.builder().mealRegisterReq(getMealRegisterReq()).restaurant(restaurant).build();
    }
}
