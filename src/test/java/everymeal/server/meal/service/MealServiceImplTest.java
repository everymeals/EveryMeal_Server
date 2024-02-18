package everymeal.server.meal.service;

import static everymeal.server.meal.MealData.getMealEntity;
import static everymeal.server.meal.MealData.getRestaurant;
import static everymeal.server.meal.MealData.getRestaurantRegisterReq;
import static everymeal.server.meal.MealData.getUniversity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.MealData;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.MealMapper;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.university.service.UniversityService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MealServiceImplTest extends IntegrationTestSupport {

    @Autowired private MealService mealService;
    @Autowired private RestaurantService restaurantService;
    @Autowired private UniversityService universityService;

    @Autowired private MealRepository mealRepository;

    @Autowired private MealMapper mealMapper;

    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private UniversityRepository universityRepository;

    @AfterEach
    void tearDown() {
        mealRepository.deleteAllInBatch();
        restaurantRepository.deleteAllInBatch();
        universityRepository.deleteAllInBatch();
    }

    @DisplayName("학생 식당을 등록합니다.")
    @Test
    void 학생식당_등록() throws Exception {
        // given
        University university = universityRepository.save(getUniversity());
        RestaurantRegisterReq req = getRestaurantRegisterReq(university.getIdx());

        // when
        Boolean response = restaurantService.createRestaurant(req);

        // then
        assertEquals(response, true);
    }

    @DisplayName("주간 식단을 등록")
    @Test
    void 주간식단_등록() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university = universityRepository.save(getUniversity());
        Restaurant restaurant =
                restaurantRepository.save(getRestaurant(university, restaurantRegisterReq));

        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.BREAKFAST.name(),
                            MealStatus.OPEN.name(),
                            today.plusDays(i),
                            10000.0,
                            MealCategory.DEFAULT.name());
            list.add(mealReq);
        }
        WeekMealRegisterReq req = new WeekMealRegisterReq(list, restaurant.getIdx());

        // when
        Boolean response = mealService.createWeekMeal(req);

        // then
        assertTrue(response);
    }

    @DisplayName("주간 식단 조회")
    @Test
    void 주간식단_조회() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university = universityRepository.save(getUniversity());
        Restaurant restaurant =
                restaurantRepository.save(getRestaurant(university, restaurantRegisterReq));

        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.parse("2023-12-16");
        for (int i = 1; i < 8; i++) {
            LocalDate offeredAt = today.plusDays(i);
            MealRegisterReq breakfast =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.BREAKFAST.name(),
                            MealStatus.OPEN.name(),
                            offeredAt,
                            10000.0,
                            MealCategory.DEFAULT.name());
            MealRegisterReq lunch =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.LUNCH.name(),
                            MealStatus.OPEN.name(),
                            offeredAt,
                            10000.0,
                            MealCategory.DEFAULT.name());
            MealRegisterReq dinner =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.DINNER.name(),
                            MealStatus.OPEN.name(),
                            offeredAt,
                            10000.0,
                            MealCategory.DEFAULT.name());
            list.add(breakfast);
            list.add(lunch);
            list.add(dinner);
        }
        WeekMealRegisterReq req = new WeekMealRegisterReq(list, restaurant.getIdx());
        mealService.createWeekMeal(req);

        // when
        String offeredAt = today.plusDays(1).toString().split("T")[0];
        var response =
                mealService.getWeekMealList(
                        university.getName(), university.getCampusName(), offeredAt);

        // then
        assertEquals(response.size(), 6);
    }

    @DisplayName("하루 식단 조회")
    @Test
    void 하루식단_조회() throws Exception {
        University university = universityRepository.save(getUniversity());
        Restaurant restaurant =
                restaurantRepository.save(getRestaurant(university, getRestaurantRegisterReq()));
        mealRepository.save(getMealEntity(restaurant));

        // when
        String offeredAt = LocalDate.now().toString().split("T")[0];
        var response =
                mealService.getDayMealListV2(
                        getUniversity().getName(), getUniversity().getCampusName(), offeredAt);

        // then
        assertEquals(response.size(), 1);
    }

    @DisplayName("학교별 학생 식당 조회")
    @Test
    void 학교별_학생_식당_조회() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university = universityRepository.save(getUniversity());
        Restaurant restaurant =
                restaurantRepository.save(getRestaurant(university, restaurantRegisterReq));

        // when
        List<RestaurantListGetRes> response = mealService.getRestaurantList(university.getIdx());
        var result = restaurantRepository.findAllByUniversityAndIsDeletedFalse(university);
        // then
        assertEquals(response.size(), result.size());
    }

    @Test
    @DisplayName("학생 식당 등록 시, 존재하지 않는 학교일 경우")
    void createRestaurantWhenUniversityIsNotFound() throws Exception {
        // given
        RestaurantRegisterReq invalidReq =
                new RestaurantRegisterReq(
                        99L,
                        "서울시 관악구 관악로",
                        "유령식당",
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 0),
                        LocalTime.of(14, 30),
                        LocalTime.of(17, 0),
                        LocalTime.of(18, 30));
        // when-then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> restaurantService.createRestaurant(invalidReq));

        assertEquals(
                applicationException.getErrorCode(), ExceptionList.UNIVERSITY_NOT_FOUND.getCODE());
    }

    @Test
    @DisplayName("주간 식단 등록 시, 존재하지 않는 식당일 경우")
    void 주간식단_등록_시_존재하지_않는_식당일_경우() throws Exception {
        // given
        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.BREAKFAST.name(),
                            MealStatus.OPEN.name(),
                            today.plusDays(i),
                            10000.0,
                            MealCategory.DEFAULT.name());
            list.add(mealReq);
        }
        WeekMealRegisterReq invalidReq = new WeekMealRegisterReq(list, 9999L);

        // when-then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class, () -> mealService.createWeekMeal(invalidReq));
        assertEquals(
                applicationException.getErrorCode(), ExceptionList.RESTAURANT_NOT_FOUND.getCODE());
    }

    @Test
    @DisplayName("등록되어 있는 식단 데이터 덮어 쓰기")
    void 등록되어_있는_식단_데이터_덮어쓰기() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university = universityRepository.save(getUniversity());
        Restaurant restaurant =
                restaurantRepository.save(getRestaurant(university, restaurantRegisterReq));

        Meal meal =
                mealRepository.save(
                        Meal.builder()
                                .mealRegisterReq(MealData.getMealRegisterReq(0))
                                .restaurant(restaurant)
                                .build());

        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.LUNCH.name(),
                            MealStatus.OPEN.name(),
                            today.plusDays(i),
                            10000.0,
                            MealCategory.DEFAULT.name());
            list.add(mealReq);
        }
        WeekMealRegisterReq invalidReq = new WeekMealRegisterReq(list, restaurant.getIdx());

        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class, () -> mealService.createWeekMeal(invalidReq));

        assertEquals(
                applicationException.getErrorCode(),
                ExceptionList.INVALID_MEAL_OFFEREDAT_REQUEST.getCODE());
    }
}
