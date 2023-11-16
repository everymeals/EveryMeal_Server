package everymeal.server.meal.repository;


import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepositoryCustom {
    List<Meal> findAllByOfferedAtOnDateAndMealType(
            LocalDate offeredAt, MealType mealType, String universityName);
    /** 23.11.16 기준 미사용 쿼리 */
    List<DayMealListGetRes> findAllByOfferedAtOnDate(LocalDate offeredAt, String universityName);

    List<WeekMealListGetRes> getWeekMealList(
            String universityName, LocalDate mondayInstant, LocalDate sundayInstant);
}
