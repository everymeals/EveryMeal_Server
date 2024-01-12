package everymeal.server.report.service;

import static everymeal.server.meal.MealData.getRestaurant;
import static everymeal.server.meal.MealData.getRestaurantRegisterReq;
import static everymeal.server.meal.MealData.getUniversity;
import static everymeal.server.review.ReviewData.getReviewEntity;
import static everymeal.server.review.ReviewData.getUserEntity;
import static org.junit.jupiter.api.Assertions.*;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.report.dto.ReportDto;
import everymeal.server.report.dto.ReportDto.ReportReviewReq;
import everymeal.server.report.entity.Report;
import everymeal.server.report.entity.ReportReason;
import everymeal.server.report.repository.ReportRepository;
import everymeal.server.review.entity.Review;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReportServiceImplTest extends IntegrationTestSupport {

    @Autowired private ReportService reportService;

    @Autowired private ReportRepository reportRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private RestaurantRepository restaurantRepository;

    @Autowired private UniversityRepository universityRepository;

    @Autowired private ReviewRepository reviewRepository;

    private University university;
    private Restaurant restaurant;
    private User reporter;
    private User reportedUser;
    private Review review;

    @BeforeEach
    void setUp() {
        university = universityRepository.save(getUniversity());
        restaurant =
                restaurantRepository.save(getRestaurant(university, getRestaurantRegisterReq()));
        reporter = userRepository.save(getUserEntity(university, 1));
        reportedUser = userRepository.save(getUserEntity(university, 2));
        review = reviewRepository.save(getReviewEntity(restaurant, reportedUser));
    }

    @AfterEach
    void tearDown() {
        reportRepository.deleteAllInBatch();
        reviewRepository.deleteAllInBatch();
        restaurantRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        universityRepository.deleteAllInBatch();
    }

    @Test
    void 학생식당리뷰_신고하기_자기_자신리뷰() throws Exception {
        // given
        ReportDto.ReportReviewReq request = new ReportReviewReq(ReportReason.UNRELATED);
        // when
        ApplicationException exception =
                assertThrows(
                        ApplicationException.class,
                        () -> {
                            reportService.reportReview(
                                    review.getIdx(), request, reportedUser.getIdx());
                        });
        // then
        assertEquals(exception.getErrorCode(), ExceptionList.REPORT_REVIEW_SELF.CODE);
    }

    @Test
    void 학생식당리뷰_신고하기() throws Exception {
        // given
        ReportDto.ReportReviewReq request = new ReportReviewReq(ReportReason.UNRELATED);
        // when
        Boolean result = reportService.reportReview(review.getIdx(), request, reporter.getIdx());
        Report report =
                reportRepository
                        .findByReviewIdxAndUserIdx(review.getIdx(), reporter.getIdx())
                        .get();
        // then
        assertTrue(result);
        assertEquals(report.getReason(), request.reason());
    }

    @Test
    void 학생식당리뷰_신고하기_이미_신고한_리뷰() throws Exception {
        // given
        ReportDto.ReportReviewReq request = new ReportReviewReq(ReportReason.UNRELATED);
        reportService.reportReview(review.getIdx(), request, reporter.getIdx());
        // when
        ApplicationException exception =
                assertThrows(
                        ApplicationException.class,
                        () -> {
                            reportService.reportReview(review.getIdx(), request, reporter.getIdx());
                        });
        // then
        assertEquals(exception.getErrorCode(), ExceptionList.REPORT_REVIEW_ALREADY.CODE);
    }

    @Test
    void 학생식당리뷰_신고하기_없는_리뷰() throws Exception {
        // given
        ReportDto.ReportReviewReq request = new ReportReviewReq(ReportReason.UNRELATED);
        // when
        ApplicationException exception =
                assertThrows(
                        ApplicationException.class,
                        () -> {
                            reportService.reportReview(0L, request, reporter.getIdx());
                        });
        // then
        assertEquals(exception.getErrorCode(), ExceptionList.REVIEW_NOT_FOUND.CODE);
    }
}
