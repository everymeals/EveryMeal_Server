package everymeal.server.review.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import everymeal.server.review.entity.Image;
import everymeal.server.review.entity.Review;
import java.util.List;
import java.util.Map;

public class ReviewDto {

    public record ReviewTodayGetRes(Long reviewIdx, String content) {}

    public static ReviewTodayGetRes of(Map<String, Object> resultMap) {
        return new ReviewTodayGetRes(
                (Long) resultMap.get("reviewIdx"), (String) resultMap.get("content"));
    }

    public record ReviewGetRes(int reviewTotalCnt, List<ReviewPaging> reviewPagingList) {}

    public record ReviewPaging(
            Long reviewIdx,
            String restaurantName,
            String nickName,
            String profileImage,
            Boolean isTodayReview,
            int grade,
            String content,
            List<String> imageList,
            int reviewMarksCnt,
            String createdAt) {}

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

    public record ReviewPagingVOWithCnt(int reviewTotalCnt, List<Review> reviewList) {}

    public record ReviewQueryParam(
            Long cursorIdx, Long restaurantIdx, int pageSize, String order, String filter) {}
}
