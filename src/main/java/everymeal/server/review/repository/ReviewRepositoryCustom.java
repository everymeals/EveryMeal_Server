package everymeal.server.review.repository;


import everymeal.server.review.dto.ReviewPagingVOWithCnt;

public interface ReviewRepositoryCustom {
    ReviewPagingVOWithCnt getReview(Long cursorIdx, Long mealIdx, int pageSize);

    ReviewPagingVOWithCnt getReviewWithNoOffSetPaging(Long cursorIdx, Long mealIdx, int pageSize);
}
