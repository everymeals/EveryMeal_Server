package everymeal.server.meal.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.entity.University;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.MealRepositoryCustom;
import everymeal.server.meal.repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealRepositoryCustom mealRepositoryCustom;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public String createRestaurant(RestaurantRegisterReq restaurantRegisterReq) {
        // 등록된 학교인지 확인
        if (Arrays.stream(University.values())
                .noneMatch(
                        university ->
                                university.name().equals(restaurantRegisterReq.getUniversity()))) {
            throw new ApplicationException(ExceptionList.INVALID_REQUEST);
        }
        University university = University.valueOf(restaurantRegisterReq.getUniversity());
        // 학생식당 생성
        Restaurant restaurant =
                Restaurant.builder()
                        .name(restaurantRegisterReq.getName())
                        .address(restaurantRegisterReq.getAddress())
                        .university(university)
                        .build();
//        restaurantRepository.save(restaurant).get_id();
        return null;
    }

    @Override
    @Transactional
    public Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq) {
        // 식당 조회
        Restaurant restaurant =
                restaurantRepository.findById(weekMealRegisterReq.getRestaurantId()).orElseThrow();

        // 주간 단위 식단 생성
        List<Meal> mealList = new ArrayList<>();
        for (MealRegisterReq req : weekMealRegisterReq.getRegisterReqList()) {
            Meal meal =
                    Meal.builder()
                            .mealStatus(MealStatus.valueOf(req.getMealStatus()))
                            .mealType(MealType.valueOf(req.getMealType()))
                            .menu(req.getMenu())
                            .restaurant(restaurant)
                            .price(req.getPrice())
                            .offeredAt(req.getOfferedAt())
                            .build();
            mealList.add(meal);
        }
        mealRepository.saveAll(mealList);
        return true;
    }

    @Override
    public List<Meal> getDayMeal(String restId) {
        // 식당 조회
        Restaurant restaurant =
                restaurantRepository
                        .findById(restId)
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
//        mealRepositoryCustom.findAllByOfferedAtToday(restaurant.get_id());
        return null;
    }
}
