package everymeal.server.review.dto;


import everymeal.server.review.entity.Review;
import java.util.List;

/**
 * ============================================================================================ 커서
 * 기반 리뷰 목록 조회 시, Repository에서 contents와 count를 함께 받아오는 VO
 * =============================================================================================
 */
public record ReviewPagingVOWithCnt(int reviewTotalCnt, List<Review> reviewList) {}
