package everymeal.server.review.service;


import everymeal.server.review.dto.request.ReviewCreateReq;
import everymeal.server.review.dto.response.ReviewDto;
import everymeal.server.review.dto.response.ReviewDto.ReviewGetRes;
import everymeal.server.review.dto.response.ReviewDto.ReviewTodayGetRes;

public interface ReviewService {
    Long createReview(ReviewCreateReq request, Long userIdx);

    Long updateReview(ReviewCreateReq request, Long userIdx, Long reviewIdx);

    Boolean deleteReview(Long userIdx, Long reviewIdx);

    ReviewGetRes getReviewWithNoOffSetPaging(ReviewDto.ReviewQueryParam queryParam);

    Boolean markReview(Long reviewIdx, boolean isLike, Long userIdx);

    ReviewTodayGetRes getTodayReview(Long restaurantIdx, String offeredAt);
}
