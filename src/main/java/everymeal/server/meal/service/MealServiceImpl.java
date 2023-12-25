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
import everymeal.server.meal.repository.MealDao;
import everymeal.server.meal.repository.MealMapper;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MealServiceImpl implements MealService {

    /**
     * ============================================================================================
     * DI
     * ============================================================================================
     */
    private final MealRepository mealRepository; // 기본 JPA 제공 DAO

    private final MealDao mealDao; // JPQL DAO
    private final MealMapper mealMapper; // MyBatis DAO
    private final UniversityRepository universityRepository;
    private final RestaurantRepository restaurantRepository;

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
                        .restaurantRegisterReq(restaurantRegisterReq)
                        .university(university)
                        .build();
        return restaurantRepository.save(restaurant).getIdx() != null;
    }

    /**
     * ============================================================================================
     * 학식 식단 등록 ( 주간, 하루 모두 등록 가능 )
     *
     * @param request 식단 등록 요청 DTO
     * @return true
     *     <p>식당이 없는 경우,
     * @throws ApplicationException 404 존재하지 않는 식당입니다. <br>
     *     REQ 데이터 중 offeredAt, Restaurant, MealType 이 동일한 데이터가 존재한다면,
     * @throws ApplicationException 400 등록되어 있는 식단 데이터 보다 과거의 날짜로 등록할 수 없습니다. <br>
     *     =========================================================================================
     */
    @Override
    @Transactional
    public Boolean createWeekMeal(WeekMealRegisterReq request) {
        // 식당 조회
        Restaurant restaurant =
                restaurantRepository
                        .findById(request.restaurantIdx())
                        .orElseThrow(
                                () -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
        // REQ 데이터 제공 날짜 기준 오름차순 정렬
        request.registerReqList().sort(Comparator.comparing(MealRegisterReq::offeredAt));
        // 식단 등록
        List<Meal> mealList = new ArrayList<>();
        for (MealRegisterReq req : request.registerReqList()) {
            // 제공날짜, 학생식당, 식사분류가 동일한 데이터가 이미 존재하면, 덮어쓰기 불가능 오류
            if (!mealMapper
                    .findAllByOfferedAtOnDateAndMealType(
                            req.offeredAt().toString(), req.mealType(), request.restaurantIdx())
                    .isEmpty()) {
                throw new ApplicationException(ExceptionList.INVALID_MEAL_OFFEREDAT_REQUEST);
            } else {
                // 데이터 생성
                Meal meal = Meal.builder().mealRegisterReq(req).restaurant(restaurant).build();
                mealList.add(meal);
            }
        }
        mealDao.saveAll(mealList);
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
                restaurantRepository.findAllByUniversityAndIsDeletedFalse(university);
        return RestaurantListGetRes.of(restaurants);
    }

    @Override
    public Map<String, Map<String, List<DayMealGetRes>>> getDayMealListV2(
            String universityName, String campusName, String offeredAt) {
        var result = mealMapper.findDayList(offeredAt, universityName, campusName);
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
            val row =
                    DayMealGetRes.of(
                            mealMapper.findDayList(i.toString(), universityName, campusName));
            result.add(Map.of(i.toString(), row));
        }
        return result;
    }
}
