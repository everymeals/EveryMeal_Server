package everymeal.server.meal.repository;


import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.entity.Meal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query(
            value =
                    "SELECT new everymeal.server.meal.controller.dto.response.DayMealListGetRes("
                            + "    COALESCE(m.idx, 0) AS mealIdx,"
                            + "    COALESCE(m.offeredAt, :offeredAt) AS offeredAt,"
                            + "    COALESCE(m.mealStatus, 'OPEN') AS mealStatus,"
                            + "    meal_types.mealType,"
                            + "    COALESCE(m.menu, '등록된 식단이 없습니다.') AS menu,"
                            + "    COALESCE(m.price, 0) AS price,"
                            + "    COALESCE(m.category, 'DEFAULT') AS category,"
                            + "    r.name AS restaurantName,"
                            + "    CONCAT(u.name, ' ', u.campusName) AS universityName) "
                            + "FROM Restaurant r "
                            + "JOIN (SELECT DISTINCT mealType AS mealType FROM Meal) AS meal_types ON 1=1 "
                            + "LEFT JOIN Meal m ON m.restaurant = r AND m.offeredAt = DATE(:offeredAt) AND m.mealType = meal_types.mealType "
                            + "INNER JOIN University u ON r.university = u "
                            + "WHERE u.name = :universityName AND u.campusName = :campusName "
                            + "ORDER BY r.name ASC")
    List<DayMealListGetRes> findDayListByOfferedAtAndUniversity(
            @Param("offeredAt") Optional<String> offeredAt,
            @Param("universityName") Optional<String> universityName,
            @Param("campusName") Optional<String> campusName);
}
