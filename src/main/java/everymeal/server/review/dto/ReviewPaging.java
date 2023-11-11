package everymeal.server.review.dto;


import java.util.List;

/**
 * ============================================================================================ 커서
 * 기반 리뷰 목록 조회 시, Response에 사용되는 하위 DTO
 * =============================================================================================
 */
public record ReviewPaging(
        Long reviewIdx,
        String restaurantName,
        String mealType,
        int grade,
        String content,
        List<String> imageList,
        int reviewMarksCnt) {}
