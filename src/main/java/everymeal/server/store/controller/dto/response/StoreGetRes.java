package everymeal.server.store.controller.dto.response;


import everymeal.server.global.util.aws.S3Util;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record StoreGetRes(
        Long idx,
        String name,
        String address,
        String phoneNumber,
        String categoryDetail,
        Integer distance,
        Double grade,
        Integer reviewCount,
        Integer recommendedCount,
        List<String> images,
        Boolean isLiked) {
    public static List<StoreGetRes> of(List<Map<String, Object>> stores) {
        return stores.stream()
                .map(
                        store -> {
                            List<String> images = null;
                            if (store.get("images") != null) {
                                images = Arrays.asList(((String) store.get("images")).split(","));
                                images.replaceAll(S3Util::getImgUrl);
                            }
                            boolean isLiked = false;
                            Integer isLikedInt = (Integer) store.get("isLiked");
                            if (isLikedInt == 1) {
                                isLiked = true;
                            }
                            return new StoreGetRes(
                                    (Long) store.get("idx"),
                                    (String) store.get("name"),
                                    (String) store.get("address"),
                                    (String) store.get("phoneNumber"),
                                    (String) store.get("categoryDetail"),
                                    (Integer) store.get("distance"),
                                    (Double) store.get("grade"),
                                    (Integer) store.get("reviewCount"),
                                    (Integer) store.get("recommendedCount"),
                                    images,
                                    isLiked);
                        })
                .toList();
    }
}
