package everymeal.server.meal.service;


import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.repository.MealDao;
import everymeal.server.meal.repository.MealMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MealCommServiceImpl {

    private final MealDao mealDao; // JPQL DAO
    private final MealMapper mealMapper; // MyBatis DAO

    public List<Meal> getMealAllByOfferedAtOnDateAndMealType(
            String offeredAt, String mealType, Long restaurantIdx) {
        return mealMapper.findAllByOfferedAtOnDateAndMealType(offeredAt, mealType, restaurantIdx);
    }

    @Transactional
    public void saveAll(List<Meal> mealList) {
        mealDao.saveAll(mealList);
    }

    public List<Map<String, Object>> getDayList(
            String offeredAt, String universityName, String campusName) {
        return mealMapper.findDayList(offeredAt, universityName, campusName);
    }
}
