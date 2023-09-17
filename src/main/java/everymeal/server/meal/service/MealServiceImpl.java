package everymeal.server.meal.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.MealRepositoryCustom;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final UniversityRepository universityRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq) {
        /**
         * 시나리오 1. 관리자에 의한 학교 등록 ( University ) - 학교 구분키 name & campusName 2. 관리자에 의한 학교별 학생식당 등록 (
         * Restaurant )
         */
        University university =
                universityRepository
                        .findByNameAndCampusNameAndIsDeletedFalse(
                                restaurantRegisterReq.getUniversityName(),
                                restaurantRegisterReq.getCampusName())
                        .stream()
                        .findFirst()
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.UNIVERSITY_NOT_FOUND));
        Restaurant restaurant =
                Restaurant.builder()
                        .name(restaurantRegisterReq.getRestaurantName())
                        .address(restaurantRegisterReq.getAddress())
                        .university(university)
                        .build();
        return restaurantRepository.save(restaurant).getIdx() != null;
    }

    @Override
    @Transactional
    public Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq) {
        // 식당 조회
        Restaurant restaurant =
                restaurantRepository
                        .findById(weekMealRegisterReq.getRestaurantIdx())
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ 데이터 제공 날짜 기준 오름차순 정렬
        weekMealRegisterReq
                .getRegisterReqList()
                .sort(Comparator.comparing(MealRegisterReq::getOfferedAt));
        /**
         * 조건) 가장 늦은 offeredAt를 기준으로 날짜가 이후인 경우에만 추가할 수 있어야 한다. ( 데이터 간 충돌 방지를 위해서 ) 가정) REQ로 들어온
         * offeredAt(식사제공날짜)가 이미 테이블 내에 포함되어 있다. 행동) REQ 중 가장 빠른 offeredAt을 기준으로 테이블 내에 데이터가 존재하는지
         * 조회 list.size() > 0 존재한다면, 오류 처리 존재하지 않는다면, 테이블에 삽입
         */
        List<Meal> meals =
                mealRepositoryCustom.findAllByAfterOfferedAt(
                        weekMealRegisterReq.getRegisterReqList().get(0), restaurant.getIdx());
        if (meals.size() > 0)
            throw new ApplicationException(ExceptionList.INVALID_MEAL_OFFEREDAT_REQUEST);
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
    public List<RestaurantListGetRes> getRestaurantList(String universityName, String campusName) {
        // 학교 등록 여부 판단
        University university =
                universityRepository
                        .findByNameAndCampusNameAndIsDeletedFalse(universityName, campusName)
                        .stream()
                        .findFirst()
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.UNIVERSITY_NOT_FOUND));
        // 학교와 식당 폐업 여부를 키로 조회
        List<Restaurant> restaurants =
                restaurantRepository.findAllByUniversityAndUseYnTrue(university);
        return RestaurantListGetRes.of(restaurants);
    }

    @Override
    public List<DayMealListGetRes> getDayMealList(Long restaurantIdx, String offeredAt) {
        // 학생 식당 등록 여부 판단
        Restaurant restaurant =
                restaurantRepository
                        .findById(restaurantIdx)
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ offeredAt에 해당하는 식단 조회
        List<Meal> meals =
                mealRepositoryCustom.findAllByOfferedAt(
                        LocalDate.parse(offeredAt), restaurant.getIdx());
        return DayMealListGetRes.of(meals);
    }

    @Override
    public List<WeekMealListGetRes> getWeekMealList(Long restaurantIdx, String offeredAt) {
        // 학생 식당 등록 여부 판단
        Restaurant restaurant =
                restaurantRepository
                        .findById(restaurantIdx)
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ offeredAt에 해당하는 식단 조회
        LocalDate startedAt = LocalDate.parse(offeredAt);
        LocalDate endedAt = startedAt.plusDays(7);
        List<Meal> meals =
                mealRepositoryCustom.findAllByBetweenOfferedAtAndEndedAt(
                        startedAt, endedAt, restaurant.getIdx());
        List<DayMealListGetRes> dayMealListGetResList = DayMealListGetRes.of(meals);
        return WeekMealListGetRes.of(dayMealListGetResList);
    }
}
