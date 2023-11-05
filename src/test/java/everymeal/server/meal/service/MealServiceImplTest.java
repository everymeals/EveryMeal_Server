package everymeal.server.meal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MealServiceImplTest extends IntegrationTestSupport {

    @Autowired private MealService mealService;

    @Autowired private MealRepository mealRepository;

    @Autowired private MealRepositoryCustom mealRepositoryCustom;

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
    void createRestaurant() throws Exception {
        // given
        RestaurantRegisterReq req = getRestaurantRegisterReq();

        // when
        University university =
                universityRepository.save(getUniversity(req.universityName(), req.campusName()));
        Boolean response = mealService.createRestaurant(req);

        // then
        Restaurant restaurants = restaurantRepository.findByName(req.restaurantName()).get();
        assertEquals(response, true);
        assertEquals(restaurants.getAddress(), req.address());
    }

    private Restaurant getRestaurant(University university, String address, String name) {
        return Restaurant.builder().university(university).address(address).name(name).build();
    }

    @DisplayName("주간 식단을 등록")
    @Test
    void createWeekMeal() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.address(),
                                restaurantRegisterReq.restaurantName()));

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
    void getWeekMealList() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.address(),
                                restaurantRegisterReq.restaurantName()));

        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
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
                            MealType.BREAKFAST.name(),
                            MealStatus.OPEN.name(),
                            offeredAt,
                            10000.0,
                            MealCategory.DEFAULT.name());
            MealRegisterReq dinner =
                    new MealRegisterReq(
                            "갈비탕, 깍두기, 흰쌀밥",
                            MealType.BREAKFAST.name(),
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
        String offeredAt = today.toString().split("T")[0];
        List<WeekMealListGetRes> response =
                mealService.getWeekMealListTest(restaurant.getIdx(), offeredAt);

        // then
        assertEquals(response.size(), 7);
    }

    @DisplayName("하루 식단 조회")
    @Test
    void getDayMealList() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.address(),
                                restaurantRegisterReq.restaurantName()));
        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        MealRegisterReq mealReq =
                new MealRegisterReq(
                        "갈비탕, 깍두기, 흰쌀밥",
                        MealType.BREAKFAST.name(),
                        MealStatus.OPEN.name(),
                        today,
                        10000.0,
                        MealCategory.DEFAULT.name());
        list.add(mealReq);
        WeekMealRegisterReq req = new WeekMealRegisterReq(list, restaurant.getIdx());
        mealService.createWeekMeal(req);

        // when
        String offeredAt = LocalDate.now().toString().split("T")[0];
        List<DayMealListGetRes> response =
                mealService.getDayMealList(restaurant.getIdx(), offeredAt);

        // then
        assertEquals(response.size(), 3);
        assertEquals(response.get(1).menu(), "등록된 식단이 없습니다.");
    }

    @DisplayName("학교별 학생 식당 조회")
    @Test
    void getRestaurantList() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.address(),
                                restaurantRegisterReq.restaurantName()));

        String universityName = restaurantRegisterReq.universityName();
        String campusName = restaurantRegisterReq.campusName();

        // when
        List<RestaurantListGetRes> response =
                mealService.getRestaurantList(universityName, campusName);

        // then
        assertEquals(response.get(0).restaurantIdx(), restaurant.getIdx());
    }

    @Test
    @DisplayName("학생 식당 등록 시, 존재하지 않는 학교일 경우")
    void createRestaurantWhenUniversityIsNotFound() throws Exception {
        // given
        RestaurantRegisterReq invalidReq =
                new RestaurantRegisterReq("서울대학교", "서울캠퍼스", "서울시 관악구 관악로", "유령식당");
        // when-then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class, () -> mealService.createRestaurant(invalidReq));

        assertEquals(
                applicationException.getErrorCode(), ExceptionList.UNIVERSITY_NOT_FOUND.getCODE());
    }

    @Test
    @DisplayName("주간 식단 등록 시, 존재하지 않는 식당일 경우")
    void createWeekMealWhenRestaurantIsNotFound() throws Exception {
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
    void createWeekMealBeforeLastMealOfferedAt() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.address(),
                                restaurantRegisterReq.restaurantName()));

        Meal meal =
                mealRepository.save(
                        Meal.builder()
                                .menu("떡볶이, 어묵탕, 튀김")
                                .mealType(MealType.LUNCH)
                                .mealStatus(MealStatus.OPEN)
                                .offeredAt(LocalDate.now())
                                .price(5000.0)
                                .category(MealCategory.DEFAULT)
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

    private RestaurantRegisterReq getRestaurantRegisterReq() {
        return new RestaurantRegisterReq("명지대학교", "인문캠퍼스", "서울시 서대문구 남가좌동 거북골로 34", "MCC 식당");
    }

    private University getUniversity(String universityName, String campusName) {
        return University.builder().name(universityName).campusName(campusName).build();
    }
}
