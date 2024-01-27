package everymeal.server.store.controller.dto.response;


import everymeal.server.global.util.TimeFormatUtil;
import everymeal.server.global.util.aws.S3Util;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record StoresGetReviews(
        Long reviewIdx,
        String content,
        Integer grade,
        LocalDateTime createdAt,
        String formattedCreatedAt,
        String nickName,
        String profileImageUrl,
        Long reviewMarksCnt,
        List<String> images,
        Long storeIdx,
        String storeName) {

    public static List<StoresGetReviews> of(List<Map<String, Object>> storesReviews) {
        return storesReviews.stream()
                .map(
                        storeReview -> {
                            List<String> images = null;
                            if (storeReview.get("images") != null) {
                                images =
                                        Arrays.asList(
                                                ((String) storeReview.get("images")).split(","));
                                images.replaceAll(S3Util::getImgUrl);
                            }
                            LocalDateTime createdAt = (LocalDateTime) storeReview.get("createdAt");
                            return new StoresGetReviews(
                                    (Long) storeReview.get("reviewIdx"),
                                    (String) storeReview.get("content"),
                                    (Integer) storeReview.get("grade"),
                                    createdAt,
                                    TimeFormatUtil.getTimeFormat(
                                            createdAt == null ? LocalDateTime.now() : createdAt),
                                    (String) storeReview.get("nickname"),
                                    (String) storeReview.get("profileImgUrl"),
                                    (Long) storeReview.get("reviewMarksCnt"),
                                    images,
                                    (Long) storeReview.get("storeIdx"),
                                    (String) storeReview.get("storeName"));
                        })
                .toList();
    }
}
