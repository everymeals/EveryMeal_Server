package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Meal;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MealDao {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Meal> mealList) {
        String sql =
                "INSERT INTO meal ( meal_status, meal_type, menu, offered_at, price, restaurant_idx, category) "
                        + "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                mealList,
                mealList.size(),
                (PreparedStatement ps, Meal meal) -> {
                    ps.setString(1, meal.getMealStatus().toString());
                    ps.setString(2, meal.getMealType().toString());
                    ps.setString(3, meal.getMenu());
                    ps.setString(4, meal.getOfferedAt().toString());
                    ps.setDouble(5, meal.getPrice());
                    ps.setLong(6, meal.getRestaurant().getIdx());
                    ps.setString(7, meal.getCategory().toString());
                });
    }
}
