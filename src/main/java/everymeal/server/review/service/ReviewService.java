package everymeal.server.review.service;


import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewGetRes;

public interface ReviewService {
    Long createReview(ReviewCreateReq request, Long userIdx);

    Long updateReview(ReviewCreateReq request, Long userIdx, Long reviewIdx);

    Boolean deleteReview(Long userIdx, Long reviewIdx);

    ReviewGetRes getReviewWithNoOffSetPaging(Long cursorIdx, Long restaurantIdx, int pageSize);

    Boolean markReview(Long reviewIdx, boolean isLike, Long userIdx);
}
