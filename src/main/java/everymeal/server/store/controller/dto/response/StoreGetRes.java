package everymeal.server.store.controller.dto.response;


import everymeal.server.store.entity.Store;
import org.springframework.data.domain.Page;

public record StoreGetRes(
        Long idx,
        String name,
        String address,
        String phoneNumber,
        Integer distance,
        Double grade,
        Integer reviewCount,
        Integer recommendedCount) {

    public static Page<StoreGetRes> of(Page<Store> stores) {
        return stores.map(
                store ->
                        new StoreGetRes(
                                store.getIdx(),
                                store.getName(),
                                store.getAddress(),
                                store.getPhone(),
                                store.getDistance(),
                                store.getGrade(),
                                store.getReviewCount(),
                                store.getRecommendedCount()));
    }
}
