package everymeal.server.review.dto;


import com.querydsl.core.annotations.QueryProjection;
import everymeal.server.review.entity.Image;
import java.util.List;

/**
 * ============================================================================================ 커서
 * 기반 리뷰 목록 조회 시, select에 사용하는 VO
 * =============================================================================================
 */
public record ReviewPagingVO(
        Long reviewIdx,
        String restaurantName,
        String mealType,
        int grade,
        String content,
        List<Image> imageList,
        int reviewMarksCnt) {

    @QueryProjection
    public ReviewPagingVO {}
}
