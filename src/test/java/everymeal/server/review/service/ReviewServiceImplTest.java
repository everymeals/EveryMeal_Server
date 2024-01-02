package everymeal.server.review.service;

import static everymeal.server.meal.MealData.getMealEntity;
import static everymeal.server.meal.MealData.getRestaurant;
import static everymeal.server.meal.MealData.getRestaurantRegisterReq;
import static everymeal.server.meal.MealData.getUniversity;
import static everymeal.server.review.ReviewData.getReviewEntity;
import static everymeal.server.review.ReviewData.getUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.review.dto.request.ReviewCreateReq;
import everymeal.server.review.dto.response.ReviewDto.ReviewQueryParam;
import everymeal.server.review.entity.Review;
import everymeal.server.review.entity.ReviewMark;
import everymeal.server.review.repository.ReviewMarkRepository;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import java.time.LocalDate;
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
    @Autowired private ReviewMarkRepository reviewMarkRepository;

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
        university = universityRepository.save(getUniversity());
        restaurant =
                restaurantRepository.save(getRestaurant(university, getRestaurantRegisterReq()));
        meal = mealRepository.save(getMealEntity(restaurant));
        user = userRepository.save(getUserEntity(university));
        review = reviewRepository.save(getReviewEntity(restaurant, user));
    }

    @AfterEach
    void tearDown() {
        reviewMarkRepository.deleteAllInBatch();
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
        assertThat(result).isNotNull();
    }

    @DisplayName("등록되지 않은 학식에 대한 리뷰를 작성할 경우, 실패한다.")
    @Test
    void createReview_failed() {
        // given
        ReviewCreateReq req = new ReviewCreateReq(0L, 5, "오늘 학식 진짜 미침", List.of(), true);
        // when
        assertThrows(
                ApplicationException.class, () -> reviewService.createReview(req, user.getIdx()));
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
        assertThat(result).isNotNull();
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
        var result =
                reviewService.getReviewWithNoOffSetPaging(
                        new ReviewQueryParam(
                                cursorIdx, restaurant.getIdx(), pageSize, "createdAt", "all"));
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
            users.add(userRepository.save(getUserEntity(university, i)));
        }
        for (User user : users) {
            reviewRepository.save(getReviewEntity(restaurant, user));
        }
    }

    @DisplayName("리뷰 좋아요")
    @Test
    void markReview() {
        // given
        Long reviewIdx = review.getIdx();
        Boolean isLike = true;
        // when
        var result = reviewService.markReview(reviewIdx, isLike, user.getIdx());
        // then
        assertThat(result).isNotNull();
    }

    @DisplayName("리뷰 싫어요")
    @Test
    void markReview_failed() {
        // given
        Long reviewIdx = review.getIdx();
        Boolean isLike = false;
        reviewMarkRepository.save(ReviewMark.builder().user(user).review(review).build());
        // when
        var result = reviewService.markReview(reviewIdx, isLike, user.getIdx());
        // then
        assertThat(result).isNotNull();
    }

    @DisplayName("오늘 먹었어요. 리뷰 조회")
    @Test
    void getTodayReview() {
        // given
        Review review = getReviewEntity(restaurant, user);
        review.updateTodayReview(true);
        Review saved = reviewRepository.save(review);
        saved.addMark(user);
        reviewRepository.saveAndFlush(saved);

        String offeredAt = LocalDate.now().toString();
        // when
        var result = reviewService.getTodayReview(restaurant.getIdx(), offeredAt);
        // then
        assertThat(result).isNotNull();
    }
}
