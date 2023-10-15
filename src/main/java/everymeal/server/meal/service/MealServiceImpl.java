package everymeal.server.meal.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetResTest;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.MealRepositoryCustom;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
    /**
     * ============================================================================================
     * GLOBAL STATIC CONSTANTS
     * =============================================================================================
     */
    private final String UN_REGISTERED_MEAL = "등록된 식단이 없습니다.";

    private final String TIME_PARSING_INFO = "T00:00:00Z";

    /**
     * ============================================================================================
     * 학생 식당 등록 <br>
     * 관리자에 의한 학교 등록이 선행되어야 합니다. University_name, University_campusName 을 구분자로 학교를 식별합니다.
     *
     * @param restaurantRegisterReq 식당 등록 요청 DTO
     * @return true
     *     <p>등록된 학교가 없는 경우,
     * @throws ApplicationException 404 등록된 학교가 아닙니다. <br>
     *     =========================================================================================
     */
    @Override
    @Transactional
    public Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq) {
        // 학교 조회
        University university =
                universityRepository
                        .findByNameAndCampusNameAndIsDeletedFalse(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName())
                        .stream()
                        .findFirst()
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.UNIVERSITY_NOT_FOUND));
        // 식당 등록
        Restaurant restaurant =
                Restaurant.builder()
                        .name(restaurantRegisterReq.restaurantName())
                        .address(restaurantRegisterReq.address())
                        .university(university)
                        .build();
        return restaurantRepository.save(restaurant).getIdx() != null;
    }

    /**
     * ============================================================================================
     * 학식 식단 등록 ( 주간, 하루 모두 등록 가능 )
     *
     * @param weekMealRegisterReq 식단 등록 요청 DTO
     * @return true
     *     <p>식당이 없는 경우,
     * @throws ApplicationException 404 존재하지 않는 식당입니다. <br>
     *     REQ 데이터 중 offeredAt, Restaurant, MealType 이 동일한 데이터가 존재한다면,
     * @throws ApplicationException 400 등록되어 있는 식단 데이터 보다 과거의 날짜로 등록할 수 없습니다. <br>
     *     =========================================================================================
     */
    @Override
    @Transactional
    public Boolean createWeekMeal(WeekMealRegisterReq weekMealRegisterReq) {
        // 식당 조회
        Restaurant restaurant =
                restaurantRepository
                        .findById(weekMealRegisterReq.restaurantIdx())
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ 데이터 제공 날짜 기준 오름차순 정렬
        weekMealRegisterReq
                .registerReqList()
                .sort(Comparator.comparing(MealRegisterReq::offeredAt));
        // 식단 등록
        List<Meal> mealList = new ArrayList<>();
        for (MealRegisterReq req : weekMealRegisterReq.registerReqList()) {
            // 제공날짜, 학생식당, 식사분류가 동일한 데이터가 이미 존재하면, 덮어쓰기 불가능 오류
            if (!mealRepositoryCustom
                    .findAllByOfferedAtOnDateAndMealType(
                            req.offeredAt(), MealType.valueOf(req.mealType()), restaurant.getIdx())
                    .isEmpty()) {
                throw new ApplicationException(ExceptionList.INVALID_MEAL_OFFEREDAT_REQUEST);
            } else {
                Instant iOfferedAt = Instant.from(req.offeredAt());
                MealStatus mealStatus =
                        req.mealStatus() == null
                                ? MealStatus.OPEN
                                : MealStatus.valueOf(req.mealStatus());
                Double price = req.price() == null ? 0.0 : req.price();
                Meal meal =
                        Meal.builder()
                                .mealStatus(mealStatus)
                                .mealType(MealType.valueOf(req.mealType()))
                                .menu(req.menu())
                                .restaurant(restaurant)
                                .price(price)
                                .offeredAt(iOfferedAt)
                                .category(MealCategory.valueOf(req.category()))
                                .build();
                mealList.add(meal);
            }
        }
        mealRepository.saveAll(mealList);
        return true;
    }
    /**
     * ============================================================================================
     * 학생식당 리스트 조회
     *
     * @param universityName 학교 한글명
     * @param campusName 캠퍼스 이름
     * @return List<RestaurantListGetRes>
     *     <p>학교가 없는 경우,
     * @throws ApplicationException 404 존재하지 않는 학교입니다. <br>
     *     =========================================================================================
     */
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
    /**
     * ============================================================================================
     * 학식 식단 Day 조회 <br>
     * 등록되지 않은 식단 데이터는 아침/점심/저녁 포맷팅에 맞게 응답 데이터를 생성해서 반환합니다.
     *
     * @param restaurantIdx 식당 아이디
     * @param offeredAt 제공 일자 --- yyyy-MM-dd
     * @return List<DayMealListGetRes>
     *     <p>식당이 없는 경우,
     * @throws ApplicationException 404 존재하지 않는 식당입니다. <br>
     *     =========================================================================================
     */
    @Override
    public List<DayMealListGetRes> getDayMealList(Long restaurantIdx, String offeredAt) {
        Instant ldOfferedAt = Instant.parse(offeredAt + TIME_PARSING_INFO);
        // 학생 식당 등록 여부 판단
        Restaurant restaurant =
                restaurantRepository
                        .findById(restaurantIdx)
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ offeredAt에 해당하는 식단 조회
        List<Meal> breakfastMeals =
                mealRepositoryCustom.findAllByOfferedAtOnDateAndMealType(
                        ldOfferedAt, MealType.BREAKFAST, restaurant.getIdx());
        List<Meal> lunchMeals =
                mealRepositoryCustom.findAllByOfferedAtOnDateAndMealType(
                        ldOfferedAt, MealType.LUNCH, restaurant.getIdx());
        List<Meal> dinnerMeals =
                mealRepositoryCustom.findAllByOfferedAtOnDateAndMealType(
                        ldOfferedAt, MealType.DINNER, restaurant.getIdx());
        // 등록되지 않은 식단 처리
        List<DayMealListGetRes> res =
                chkEmptyDayMeal(
                        ldOfferedAt, restaurant.getName(), breakfastMeals, lunchMeals, dinnerMeals);
        return res;
    }

    /**
     * ============================================================================================
     * 학식 식단 Week 조회 <br>
     * 등록되지 않은 식단 데이터는 아침/점심/저녁 포맷팅에 맞게 응답 데이터를 생성해서 반환합니다. ( 7일 데이터 )
     *
     * @param restaurantIdx 식당 아이디
     * @param offeredAt 제공일자 --- yyyy-MM-dd
     * @return List<DayMealListGetRes>
     *     <p>식당이 없는 경우,
     * @throws ApplicationException 404 존재하지 않는 식당입니다. <br>
     *     =========================================================================================
     */
    @Override
    public List<WeekMealListGetRes> getWeekMealList(Long restaurantIdx, String offeredAt) {
        // 학생 식당 등록 여부 판단
        Restaurant restaurant =
                restaurantRepository
                        .findById(restaurantIdx)
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ offeredAt을 시작 일자로 주간 단위 식단 조회
        Instant startedAt = Instant.parse(offeredAt + TIME_PARSING_INFO);
        Instant endedAt = startedAt.plus(6, ChronoUnit.DAYS);
        List<Instant> dateList = new ArrayList<>();
        Instant current = startedAt;
        while (!current.isAfter(endedAt)) {
            dateList.add(current);
            current = current.plus(1, ChronoUnit.DAYS);
        }
        // 비동기로 아침/점심/저녁 조회 쿼리 수행
        List<CompletableFuture<List<DayMealListGetRes>>> mealFutures =
            dateList.stream()
                .map(
                    ldOfferedAt -> {
                        CompletableFuture<List<Meal>> breakfastFuture =
                            getMealsByDateAndTypeAsync(
                                ldOfferedAt.truncatedTo(ChronoUnit.DAYS),
                                MealType.BREAKFAST,
                                restaurant.getIdx());
                        CompletableFuture<List<Meal>> lunchFuture =
                            getMealsByDateAndTypeAsync(
                                ldOfferedAt.truncatedTo(ChronoUnit.DAYS),
                                MealType.LUNCH,
                                restaurant.getIdx());
                        CompletableFuture<List<Meal>> dinnerFuture =
                            getMealsByDateAndTypeAsync(
                                ldOfferedAt.truncatedTo(ChronoUnit.DAYS),
                                MealType.DINNER,
                                restaurant.getIdx());

                        return CompletableFuture.allOf(
                                breakfastFuture, lunchFuture, dinnerFuture)
                            .thenApply(
                                ignored -> {
                                    List<Meal> breakfastMeals =
                                        breakfastFuture.join();
                                    List<Meal> lunchMeals = lunchFuture.join();
                                    List<Meal> dinnerMeals =
                                        dinnerFuture.join();

                                    // 등록되지 않은 식단 처리
                                    return chkEmptyDayMeal(
                                        ldOfferedAt.truncatedTo(
                                            ChronoUnit.DAYS),
                                        restaurant.getName(),
                                        breakfastMeals,
                                        lunchMeals,
                                        dinnerMeals);
                                });
                    })
                .toList();
        // CompletableFuture를 모두 조합하고 결과를 가져옴
        List<DayMealListGetRes> res =
            mealFutures.stream().map(CompletableFuture::join).flatMap(List::stream).toList();
        return WeekMealListGetRes.of(res);
    }

    @Override
    public List<WeekMealListGetResTest> getWeekMealListTest(Long restaurantIdx, String offeredAt) {
        Restaurant restaurant =
            restaurantRepository
                .findById(restaurantIdx)
                .orElseThrow(
                    () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));

        // 현재 날짜와 시간을 가져옵니다.
        LocalDateTime now = LocalDateTime.now().plusDays(1);

        // 현재 요일을 가져옵니다.
        DayOfWeek currentDayOfWeek = now.getDayOfWeek();

        // 월요일과 일요일의 날짜를 계산합니다.
        LocalDateTime monday;
        LocalDateTime sunday;

        if (currentDayOfWeek == DayOfWeek.MONDAY) {
            // 현재 요일이 월요일인 경우, 현재 날짜를 월요일로 설정하고 일요일을 6일 후로 설정합니다.
            monday = now;
            sunday = now.plusDays(6);
        } else {
            // 그 외의 경우, 현재 요일로부터 월요일과 일요일을 계산합니다.
            monday = now.minusDays(currentDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());
            sunday = now.plusDays(DayOfWeek.SUNDAY.getValue() - currentDayOfWeek.getValue());
        }

        // LocalDateTime을 한국 시간 (Asia/Seoul)으로 변환한 다음 Instant로 변환합니다.
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        Instant mondayInstant = monday.atZone(seoulZoneId).toInstant();
        Instant sundayInstant = sunday.atZone(seoulZoneId).toInstant();

        return mealRepositoryCustom.getWeekMealList(restaurant,
            mondayInstant, sundayInstant);
    }

    /**
     * ============================================================================================
     * 비동기 식사 조회 <br>
     * 일별 아침/점심/저녁에 따른 식사 조회 쿼리를 수행합니다.
     *
     * @param restaurantIdx 식당 아이디
     * @param date 제공일자 --- yyyy-MM-dd
     * @param mealType 식사 구분 ( 아침/점심/저녁 )
     * @return CompletableFuture<List<Meal>>
     *     =========================================================================================
     */
    @Async
    public CompletableFuture<List<Meal>> getMealsByDateAndTypeAsync(
            Instant date, MealType mealType, Long restaurantIdx) {
        // 비동기로 아침/점심/저녁 조회 쿼리 수행
        List<Meal> meals =
                mealRepositoryCustom.findAllByOfferedAtOnDateAndMealType(
                        date, mealType, restaurantIdx);
        return CompletableFuture.completedFuture(meals);
    }
    /**
     * ============================================================================================
     * 등록되지 않은 식단 존재 여부 확인 <br>
     * 등록되지 않은 식단이 존재하는 경우, 응답을 위한 더미 데이터를 생성합니다.
     *
     * @param offeredAt 제공일자
     * @param restaurantName 식당명
     * @param breakfastMeals 아침 식사
     * @param lunchMeals 점심 식사
     * @param dinnerMeals 저녁 식사
     * @return List<DayMealListGetRes>
     *     =========================================================================================
     */
    private List<DayMealListGetRes> chkEmptyDayMeal(
            Instant offeredAt,
            String restaurantName,
            List<Meal> breakfastMeals,
            List<Meal> lunchMeals,
            List<Meal> dinnerMeals) {
        List<DayMealListGetRes> res = new ArrayList<>();
        if (breakfastMeals.isEmpty()) {
            res.add(
                    new DayMealListGetRes(
                            UN_REGISTERED_MEAL,
                            MealType.BREAKFAST.getValue(),
                            MealStatus.OPEN.getValue(),
                            offeredAt,
                            0.0,
                            MealCategory.DEFAULT.getValue(),
                            restaurantName));
        } else {
            res.addAll(DayMealListGetRes.of(breakfastMeals));
        }
        if (lunchMeals.isEmpty()) {
            res.add(
                    new DayMealListGetRes(
                            UN_REGISTERED_MEAL,
                            MealType.LUNCH.getValue(),
                            MealStatus.OPEN.getValue(),
                            offeredAt,
                            0.0,
                            MealCategory.DEFAULT.getValue(),
                            restaurantName));
        } else {
            res.addAll(DayMealListGetRes.of(lunchMeals));
        }
        if (dinnerMeals.isEmpty()) {
            res.add(
                    new DayMealListGetRes(
                            UN_REGISTERED_MEAL,
                            MealType.DINNER.getValue(),
                            MealStatus.OPEN.getValue(),
                            offeredAt,
                            0.0,
                            MealCategory.DEFAULT.getValue(),
                            restaurantName));
        } else {
            res.addAll(DayMealListGetRes.of(dinnerMeals));
        }
        return res;
    }
}
