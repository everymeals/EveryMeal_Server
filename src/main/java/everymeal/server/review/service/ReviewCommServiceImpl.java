package everymeal.server.review.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.review.dto.response.ReviewDto;
import everymeal.server.review.dto.response.ReviewDto.ReviewPagingVOWithCnt;
import everymeal.server.review.entity.Review;
import everymeal.server.review.entity.ReviewMark;
import everymeal.server.review.repository.ReviewMapper;
import everymeal.server.review.repository.ReviewMarkRepository;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.user.entity.User;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommServiceImpl {

    private final ReviewRepository reviewRepository;
    private final ReviewMarkRepository reviewMarkRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Review getReviewEntity(Long reviewIdx) {
        return reviewRepository
                .findById(reviewIdx)
                .orElseThrow(() -> new ApplicationException(ExceptionList.REVIEW_NOT_FOUND));
    }

    public Review getReviewEntity(Long reviewIdx, User user) {
        return reviewRepository
                .findByIdxAndUser(reviewIdx, user)
                .orElseThrow(() -> new ApplicationException(ExceptionList.REVIEW_NOT_FOUND));
    }

    public Optional<Review> getReviewOptionalEntity(Long reviewIdx) {
        return reviewRepository.findById(reviewIdx);
    }

    public ReviewPagingVOWithCnt getReviewPagingVOWithCnt(ReviewDto.ReviewQueryParam queryParam) {
        return reviewRepository.getReview(queryParam);
    }

    public Optional<ReviewMark> getReviewMarkOptionalEntity(Long reviewIdx, Long userIdx) {
        return reviewMarkRepository.findByReviewIdxAndUserIdx(reviewIdx, userIdx);
    }

    @Transactional
    public void deleteReviewMark(ReviewMark reviewMark) {
        reviewMarkRepository.delete(reviewMark);
    }

    public Map<String, Object> getTodayReviewEntityFromMapper(
            Long restaurantIdx, String offeredAt) {
        Map<String, Object> todayReview = reviewMapper.findTodayReview(restaurantIdx, offeredAt);
        System.out.println("todayReview = " + todayReview);
        return todayReview;
    }

    @Transactional
    public Review saveAndFlush(Review review) {
        return reviewRepository.saveAndFlush(review);
    }
}
