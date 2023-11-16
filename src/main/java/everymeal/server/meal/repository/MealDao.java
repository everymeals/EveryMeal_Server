package everymeal.server.meal.repository;


import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MealDao {
    private final EntityManager em;

    public Map<String, List<DayMealListGetRes>> findDayListByOfferedAtAndUniversity(
            Optional<String> offeredAt,
            Optional<String> universityName,
            Optional<String> campusName) {
        // 쿼리 생성
        String query =
                "SELECT NEW everymeal.server.meal.controller.dto.response.DayMealListGetRes("
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
                        + "WHERE "
                        + " u.name = :universityName AND u.campusName = :campusName "
                        + "ORDER BY r.name ASC";
        // 쿼리 옵션 추가 및 실행
        List<DayMealListGetRes> result =
                em.createQuery(query, DayMealListGetRes.class)
                        .setParameter("offeredAt", offeredAt.orElse(null))
                        .setParameter("universityName", universityName.orElse(null))
                        .setParameter("campusName", campusName.orElse(null))
                        .getResultList();
        // 결과 반환용 변수 선언
        Map<String, List<DayMealListGetRes>> map = new HashMap<>();
        for (DayMealListGetRes meal : result) {
            String restaurantName = meal.restaurantName();

            // 해당 레스토랑 이름으로 맵에서 리스트를 가져오거나 없으면 새 리스트 생성
            List<DayMealListGetRes> restaurantList =
                    map.computeIfAbsent(restaurantName, k -> new ArrayList<>());

            // 현재 Meal을 레스토랑 리스트에 추가
            restaurantList.add(meal);
        }

        return map;
    }
}
