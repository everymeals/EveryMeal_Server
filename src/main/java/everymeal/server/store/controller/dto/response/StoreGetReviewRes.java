package everymeal.server.store.controller.dto.response;


import everymeal.server.global.util.aws.S3Util;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record StoreGetReviewRes(
        Integer reviewIdx,
        String content,
        Integer grade,
        LocalDateTime createdAt,
        String nickName,
        String profileImageUrl,
        Integer recommendedCount,
        List<String> images) {

    public static List<StoreGetReviewRes> of(List<Map<String, Object>> storeReview) {
        return storeReview.stream()
                .map(
                        review -> {
                            List<String> images = null;
                            if (review.get("images") != null) {
                                images = Arrays.asList(((String) review.get("images")).split(","));
                                images.replaceAll(S3Util::getImgUrl);
                            }
                            return new StoreGetReviewRes(
                                    (Integer) review.get("reviewIdx"),
                                    (String) review.get("content"),
                                    (Integer) review.get("grade"),
                                    (LocalDateTime) review.get("createdAt"),
                                    (String) review.get("nickName"),
                                    S3Util.getImgUrl((String) review.get("profileImageUrl")),
                                    (Integer) review.get("recommendedCount"),
                                    images);
                        })
                .toList();
    }
}
