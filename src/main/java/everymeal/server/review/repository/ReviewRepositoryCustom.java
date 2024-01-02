package everymeal.server.review.repository;


import everymeal.server.review.dto.response.ReviewDto;
import everymeal.server.review.dto.response.ReviewDto.ReviewPagingVOWithCnt;

public interface ReviewRepositoryCustom {
    ReviewPagingVOWithCnt getReview(ReviewDto.ReviewQueryParam queryParam);
}
