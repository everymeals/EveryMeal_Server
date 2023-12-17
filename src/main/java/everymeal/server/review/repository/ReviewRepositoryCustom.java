package everymeal.server.review.repository;


import everymeal.server.review.dto.ReviewPagingVOWithCnt;

public interface ReviewRepositoryCustom {
    ReviewPagingVOWithCnt getReview(Long cursorIdx, Long restaurantIdx, int pageSize);
}
