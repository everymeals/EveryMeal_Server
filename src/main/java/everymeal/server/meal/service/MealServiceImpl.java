package everymeal.server.meal.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.Restaurant;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MealServiceImpl implements MealService {

    private final MealCommServiceImpl mealCommServiceImpl;
    private final RestaurantService restaurantServiceImpl;

    @Override
    @Transactional
    public Boolean createWeekMeal(WeekMealRegisterReq request) {
        // 식당 조회
        Restaurant restaurant = restaurantServiceImpl.getRestaurant(request.restaurantIdx());
        // REQ 데이터 제공 날짜 기준 오름차순 정렬
        request.registerReqList().sort(Comparator.comparing(MealRegisterReq::offeredAt));
        // 식단 등록
        List<Meal> mealList = new ArrayList<>();
        for (MealRegisterReq req : request.registerReqList()) {
            // 제공날짜, 학생식당, 식사분류가 동일한 데이터가 이미 존재하면, 덮어쓰기 불가능 오류
            if (!mealCommServiceImpl
                    .getMealAllByOfferedAtOnDateAndMealType(
                            req.offeredAt().toString(), req.mealType(), request.restaurantIdx())
                    .isEmpty()) {
                throw new ApplicationException(ExceptionList.INVALID_MEAL_OFFEREDAT_REQUEST);
            } else {
                // 데이터 생성
                Meal meal = Meal.builder().mealRegisterReq(req).restaurant(restaurant).build();
                mealList.add(meal);
            }
        }
        mealCommServiceImpl.saveAll(mealList);
        return true;
    }

    @Override
    public List<RestaurantListGetRes> getRestaurantList(Long universityIdx) {
        // 학교와 식당 폐업 여부를 키로 조회
        List<Restaurant> restaurants =
                restaurantServiceImpl.getAllByUniversityAndIsDeletedFalse(universityIdx);
        return RestaurantListGetRes.of(restaurants);
    }

    @Override
    public Map<String, Map<String, List<DayMealGetRes>>> getDayMealListV2(
            String universityName, String campusName, String offeredAt) {
        var result = mealCommServiceImpl.getDayList(offeredAt, universityName, campusName);
        return Map.of(offeredAt, DayMealGetRes.of(result));
    }

    @Override
    public List<Map<String, Map<String, List<DayMealGetRes>>>> getWeekMealList(
            String universityName, String campusName, String offeredAt) { // 현재 날짜와 시간을 가져옵니다.
        LocalDate ldOfferedAt = LocalDate.parse(offeredAt);

        // 현재 요일을 가져옵니다.
        DayOfWeek currentDayOfWeek = ldOfferedAt.getDayOfWeek();

        // 월요일과 일요일의 날짜를 계산합니다.
        LocalDate monday;
        LocalDate sunday;

        if (currentDayOfWeek == DayOfWeek.MONDAY) {
            // 현재 요일이 월요일인 경우, 현재 날짜를 월요일로 설정하고 일요일을 6일 후로 설정합니다.
            monday = ldOfferedAt;
            sunday = ldOfferedAt.plusDays(6);
        } else {
            // 그 외의 경우, 현재 요일로부터 월요일과 일요일을 계산합니다.
            monday =
                    ldOfferedAt.minusDays(
                            currentDayOfWeek.getValue() - (long) DayOfWeek.MONDAY.getValue());
            sunday =
                    ldOfferedAt.plusDays(
                            DayOfWeek.SUNDAY.getValue() - (long) currentDayOfWeek.getValue());
        }
        List<Map<String, Map<String, List<DayMealGetRes>>>> result = new ArrayList<>();
        for (LocalDate i = monday; i.isBefore(sunday); i = i.plusDays(1)) {
            Map<String, List<DayMealGetRes>> row =
                    DayMealGetRes.of(
                            mealCommServiceImpl.getDayList(
                                    i.toString(), universityName, campusName));
            result.add(Map.of(i.toString(), row));
        }
        return result;
    }

    @Override
    public Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq) {
        return restaurantServiceImpl.createRestaurant(restaurantRegisterReq);
    }
}
