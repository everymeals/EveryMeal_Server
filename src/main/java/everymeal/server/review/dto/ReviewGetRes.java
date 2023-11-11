package everymeal.server.review.dto;


import java.util.List;

/**
 * ============================================================================================ 커서
 * 기반 리뷰 목록 조회 시, Response DTO
 * =============================================================================================
 */
public record ReviewGetRes(int reviewTotalCnt, List<ReviewPaging> reviewPagingList) {}
