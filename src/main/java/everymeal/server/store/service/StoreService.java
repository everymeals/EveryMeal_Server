package everymeal.server.store.service;


import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface StoreService {

    Page<StoreGetRes> getStores(
            Long campusIdx, Pageable of, String group, Long userIdx, String order, Integer grade);

    Page<LikedStoreGetRes> getUserLikesStore(
            Long campusIdx, Pageable of, String group, AuthenticatedUser authenticatedUser);

    Boolean likesStore(Long storeIdx, AuthenticatedUser authenticatedUser);

    Page<StoreGetRes> getStoresKeyword(
            Long campusIdx, String keyword, Long idx, PageRequest pageRequest);
}
