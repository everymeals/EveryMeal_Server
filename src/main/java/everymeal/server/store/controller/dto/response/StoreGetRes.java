package everymeal.server.store.controller.dto.response;


import everymeal.server.store.entity.Store;
import java.util.List;
import org.springframework.data.domain.Page;

public record StoreGetRes(
        Long idx,
        String name,
        String address,
        String phoneNumber,
        String categoryDetail,
        Integer distance,
        Double grade,
        Integer reviewCount,
        Integer recommendedCount) {

    public static List<StoreGetRes> of(List<Store> stores) {
        return stores.stream().map(
                store ->
                        new StoreGetRes(
                                store.getIdx(),
                                store.getName(),
                                store.getAddress(),
                                store.getPhone(),
                                store.getCategoryDetail(),
                                store.getDistance(),
                                store.getGrade(),
                                store.getReviewCount(),
                                store.getRecommendedCount())).toList();
    }
}
