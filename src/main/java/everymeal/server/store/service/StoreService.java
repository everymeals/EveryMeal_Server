package everymeal.server.store.service;


import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetReviewRes;
import everymeal.server.store.controller.dto.response.StoresGetReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface StoreService {

    Page<StoreGetRes> getStores(
            Long campusIdx, Pageable of, String group, Long userIdx, String order, Integer grade);

    StoreGetRes getStore(Long storeIdx, Long userIdx);

    Page<LikedStoreGetRes> getUserLikesStore(
            Long campusIdx, Pageable of, String group, Long userIdx);

    Boolean likesStore(Long storeIdx, Long userIdx);

    Page<StoreGetRes> getStoresKeyword(
            Long campusIdx, String keyword, Long idx, PageRequest pageRequest);

    Page<StoreGetReviewRes> getStoreReview(Long storeIdx, Long aLong, PageRequest of);

    Page<StoresGetReviews> getStoresReviews(
            PageRequest pageRequest, String order, String group, Integer grade);
}
