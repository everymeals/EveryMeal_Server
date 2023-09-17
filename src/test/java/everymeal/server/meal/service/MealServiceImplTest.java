package everymeal.server.meal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.controller.dto.request.WeekMealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.RestaurantListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
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
                universityRepository.save(
                        getUniversity(req.getUniversityName(), req.getCampusName()));
        Boolean response = mealService.createRestaurant(req);

        // then
        Restaurant restaurants = restaurantRepository.findByName(req.getRestaurantName()).get();
        assertEquals(response, true);
        assertEquals(restaurants.getAddress(), req.getAddress());
    }

    private Restaurant getRestaurant(University university, String address, String name) {
        return Restaurant.builder().university(university).address(address).name(name).build();
    }

    @DisplayName("주간 식단을 등록합니다.")
    @Test
    void createWeekMeal() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.getUniversityName(),
                                restaurantRegisterReq.getCampusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.getAddress(),
                                restaurantRegisterReq.getRestaurantName()));

        WeekMealRegisterReq req = new WeekMealRegisterReq();
        req.setRestaurantIdx(restaurant.getIdx());
        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq = new MealRegisterReq();
            mealReq.setMealStatus(MealStatus.OPEN.name());
            mealReq.setMenu("갈비탕, 깍두기, 흰쌀밥");
            mealReq.setMealType(MealType.BREAKFAST.name());
            mealReq.setPrice(10000.0);
            mealReq.setOfferedAt(today.plusDays(i));
            list.add(mealReq);
        }
        req.setRegisterReqList(list);

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
                                restaurantRegisterReq.getUniversityName(),
                                restaurantRegisterReq.getCampusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.getAddress(),
                                restaurantRegisterReq.getRestaurantName()));

        WeekMealRegisterReq req = new WeekMealRegisterReq();
        req.setRestaurantIdx(restaurant.getIdx());
        List<MealRegisterReq> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq = new MealRegisterReq();
            mealReq.setMealStatus(MealStatus.OPEN.name());
            mealReq.setMenu("갈비탕, 깍두기, 흰쌀밥");
            mealReq.setMealType(MealType.BREAKFAST.name());
            mealReq.setPrice(10000.0);
            mealReq.setOfferedAt(today.plusDays(i));
            list.add(mealReq);
        }
        req.setRegisterReqList(list);
        mealService.createWeekMeal(req);

        // when
        List<WeekMealListGetRes> response =
                mealService.getWeekMealList(restaurant.getIdx(), today.toString());

        // then
        assertEquals(response.size(), req.getRegisterReqList().size());
    }

    @DisplayName("하루 식단 조회")
    @Test
    void getDayMealList() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.getUniversityName(),
                                restaurantRegisterReq.getCampusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.getAddress(),
                                restaurantRegisterReq.getRestaurantName()));

        WeekMealRegisterReq req = new WeekMealRegisterReq();
        req.setRestaurantIdx(restaurant.getIdx());
        List<MealRegisterReq> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            MealRegisterReq mealReq = new MealRegisterReq();
            mealReq.setMealStatus(MealStatus.OPEN.name());
            mealReq.setMenu("갈비탕, 깍두기, 흰쌀밥");
            mealReq.setMealType(MealType.BREAKFAST.name());
            mealReq.setPrice(10000.0);
            mealReq.setOfferedAt(LocalDate.now());
            list.add(mealReq);
        }
        req.setRegisterReqList(list);
        mealService.createWeekMeal(req);

        // when
        List<DayMealListGetRes> response =
                mealService.getDayMealList(restaurant.getIdx(), LocalDate.now().toString());

        // then
        assertEquals(response.size(), req.getRegisterReqList().size());
    }

    @DisplayName("학교별 학생 식당 조회")
    @Test
    void getRestaurantList() throws Exception {
        // given
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        University university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.getUniversityName(),
                                restaurantRegisterReq.getCampusName()));
        Restaurant restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.getAddress(),
                                restaurantRegisterReq.getRestaurantName()));

        String universityName = restaurantRegisterReq.getUniversityName();
        String campusName = restaurantRegisterReq.getCampusName();

        // when
        List<RestaurantListGetRes> response =
                mealService.getRestaurantList(universityName, campusName);

        // then
        assertEquals(response.get(0).getRestaurantIdx(), restaurant.getIdx());
    }

    private RestaurantRegisterReq getRestaurantRegisterReq() {
        RestaurantRegisterReq req = new RestaurantRegisterReq();
        req.setRestaurantName("MCC 식당");
        req.setAddress("서울시 서대문구 남가좌동 거북골로 34");
        req.setUniversityName("명지대학교");
        req.setCampusName("인문캠퍼스");
        return req;
    }

    private University getUniversity(String universityName, String campusName) {
        return University.builder()
                .name(universityName)
                .campusName(campusName)
                .isDeleted(false)
                .build();
    }
}
