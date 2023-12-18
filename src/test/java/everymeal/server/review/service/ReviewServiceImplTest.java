package everymeal.server.review.service;

import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.entity.Review;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewServiceImplTest extends IntegrationTestSupport {

    @Autowired private ReviewService reviewService;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private MealRepository mealRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private UserRepository userRepository;

    /**
     * ============================================================================================
     * PRIVATE VARIABLE FOR TEST
     * =============================================================================================
     */
    private University university;

    private Meal meal;

    private Restaurant restaurant;
    private User user;
    private Review review;

    @BeforeEach
    void createDummyForTest() {
        RestaurantRegisterReq restaurantRegisterReq = getRestaurantRegisterReq();
        university =
                universityRepository.save(
                        getUniversity(
                                restaurantRegisterReq.universityName(),
                                restaurantRegisterReq.campusName()));
        restaurant =
                restaurantRepository.save(
                        getRestaurant(
                                university,
                                restaurantRegisterReq.address(),
                                restaurantRegisterReq.restaurantName()));
        meal = mealRepository.save(getMeal(restaurant));
        user = userRepository.save(getUser(university, 1));
        review = reviewRepository.save(getReview(user));
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAllInBatch();
        mealRepository.deleteAllInBatch();
        restaurantRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        universityRepository.deleteAllInBatch();
    }

    @DisplayName("리뷰를 작성에 성공한다. - 오늘 먹은 거")
    @Test
    void createReview() {
        // given
        ReviewCreateReq req =
                new ReviewCreateReq(restaurant.getIdx(), 5, "오늘 학식 진짜 미침", List.of(), true);
        // when
        var result = reviewService.createReview(req, user.getIdx());

        // then
        assertEquals(result, Boolean.TRUE);
    }

    @DisplayName("등록되지 않은 학식에 대한 리뷰를 작성할 경우, 실패한다.")
    @Test
    void createReview_failed() {
        // given
        ReviewCreateReq req = new ReviewCreateReq(0L, 5, "오늘 학식 진짜 미침", List.of(), true);
        // when
        var result = reviewService.createReview(req, user.getIdx());

        // then
        assertEquals(result, Boolean.FALSE);
    }

    @DisplayName("학식 리뷰를 수정한다.")
    @Test
    void updateReview() {
        // given
        ReviewCreateReq req =
                new ReviewCreateReq(restaurant.getIdx(), 5, "오늘 학식 진짜 미침", List.of(), true);
        // when
        var result = reviewService.updateReview(req, user.getIdx(), review.getIdx());
        var updated = reviewRepository.findById(review.getIdx());
        // then
        assertEquals(result, Boolean.TRUE);
        assertEquals(updated.get().getContent(), req.content());
    }

    @DisplayName("등록되지 않은 리뷰를 수정할 경우, 실패한다.")
    @Test
    void updateReview_failed() {
        // given
        ReviewCreateReq req =
                new ReviewCreateReq(restaurant.getIdx(), 5, "오늘 학식 진짜 미침", List.of(), true);
        // when then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> reviewService.updateReview(req, user.getIdx(), 0L));

        // then
        assertEquals(applicationException.getErrorCode(), ExceptionList.REVIEW_NOT_FOUND.getCODE());
    }

    @DisplayName("리뷰를 삭제한다.")
    @Test
    void deleteReview() {
        // given

        // when
        var result = reviewService.deleteReview(user.getIdx(), review.getIdx());
        var deleted = reviewRepository.findById(review.getIdx());
        // then
        assertEquals(result, Boolean.TRUE);
        assertEquals(deleted.get().isDeleted(), Boolean.TRUE);
    }

    @DisplayName("등록되지 않은 리뷰를 삭제할 경우, 실패한다.")
    @Test
    void deleteReview_failed() {
        // given

        // when then
        ApplicationException applicationException =
                assertThrows(
                        ApplicationException.class,
                        () -> reviewService.deleteReview(user.getIdx(), 0L));

        // then
        assertEquals(applicationException.getErrorCode(), ExceptionList.REVIEW_NOT_FOUND.getCODE());
    }

    @DisplayName("학식에 대한 리뷰를 커서 기반 페이징으로 조회합니다.")
    @Test
    void getReviewWithNoOffSetPaging() {
        // given
        createDummy();
        Long cursorIdx = 1L;
        Long mealIdx = meal.getIdx();
        int pageSize = 8;
        // when
        var result = reviewService.getReviewWithNoOffSetPaging(cursorIdx, mealIdx, pageSize);
        // then
        assertEquals(result.reviewPagingList().size(), pageSize);
    }
    /**
     * ============================================================================================
     * PRIVATE FUNCTION --더미 데이터 생성용
     * =============================================================================================
     */
    private void createDummy() {
        List<User> users = new ArrayList<>();
        for (int i = 2; i < 12; i++) {
            users.add(userRepository.save(getUser(university, i)));
        }
        for (User user : users) {
            reviewRepository.save(getReview(user));
        }
    }

    private Review getReview(User user) {
        return Review.builder()
                .user(user)
                .grade(4)
                .images(List.of())
                .restaurant(restaurant)
                .content("Good")
                .build();
    }

    private User getUser(University university, int uniqueIdx) {
        return User.builder()
                .email(uniqueIdx + "test@gmail.com")
                .university(university)
                .nickname(uniqueIdx + "띵랑이")
                .profileImgUrl("img.url")
                .build();
    }

    private Meal getMeal(Restaurant restaurant) {
        return Meal.builder()
                .mealType(MealType.BREAKFAST)
                .mealStatus(MealStatus.OPEN)
                .offeredAt(LocalDate.now())
                .price(10000.0)
                .category(MealCategory.DEFAULT)
                .restaurant(restaurant)
                .build();
    }

    private RestaurantRegisterReq getRestaurantRegisterReq() {
        return new RestaurantRegisterReq(
                "명지대학교",
                "인문캠퍼스",
                "서울시 서대문구 남가좌동 거북골로 34",
                "MCC 식당",
                LocalTime.of(8, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(14, 30),
                LocalTime.of(17, 0),
                LocalTime.of(18, 30));
    }

    private University getUniversity(String universityName, String campusName) {
        return University.builder().name(universityName).campusName(campusName).build();
    }

    private Restaurant getRestaurant(University university, String address, String name) {
        return Restaurant.builder().university(university).address(address).name(name).build();
    }
}
