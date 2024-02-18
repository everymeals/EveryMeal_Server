package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Meal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MealMapper {
    List<Map<String, Object>> findDayList(
            @Param("offeredAt") String offeredAt, @Param("universityIdx") Long universityIdx);

    List<Meal> findAllByOfferedAtOnDateAndMealType(
            @Param("offeredAt") String offeredAt,
            @Param("mealType") String mealType,
            @Param("restaurantIdx") Long restaurantIdx);
}
