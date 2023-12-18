package everymeal.server.review.service;


import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewGetRes;

public interface ReviewService {
    Boolean createReview(ReviewCreateReq request, Long userIdx);

    Boolean updateReview(ReviewCreateReq request, Long userIdx, Long reviewIdx);

    Boolean deleteReview(Long userIdx, Long reviewIdx);

    ReviewGetRes getReviewWithNoOffSetPaging(Long cursorIdx, Long restaurantIdx, int pageSize);
}
