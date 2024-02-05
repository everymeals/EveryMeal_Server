package everymeal.server.store.service;

import static everymeal.server.global.exception.ExceptionList.STORE_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.review.entity.Image;
import everymeal.server.review.repository.ImageRepository;
import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetReviewRes;
import everymeal.server.store.controller.dto.response.StoresGetReviews;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreMapper;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.store.repository.StoreRepositoryCustom;
import everymeal.server.user.entity.Like;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.LikeRepository;
import everymeal.server.user.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final StoreRepositoryCustom storeRepositoryCustom;

    @Override
    public Page<StoreGetRes> getStores(
            Long campusIdx,
            Pageable pageable,
            String group,
            Long userIdx,
            String order,
            Integer grade) {
        List<Map<String, Object>> stores =
                storeMapper.getStores(
                        campusIdx,
                        pageable.getPageSize(),
                        pageable.getOffset(),
                        group,
                        userIdx,
                        order,
                        grade);
        List<StoreGetRes> result = StoreGetRes.of(stores);
        Long count =
                storeMapper.getStoreCount(
                        campusIdx,
                        pageable.getPageSize(),
                        pageable.getOffset(),
                        group,
                        userIdx,
                        order,
                        grade);
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public StoreGetRes getStore(Long storeIdx, Long userIdx) {
        Store store =
                storeRepository
                        .findById(storeIdx)
                        .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        boolean isLike = false;
        if (userIdx != null) {
            isLike = likeRepository.findByUserIdxAndStoreIdx(userIdx, storeIdx).isPresent();
        }
        List<Image> images = imageRepository.getStoreImages(storeIdx);
        return StoreGetRes.of(store, isLike, images);
    }

    @Override
    public Page<LikedStoreGetRes> getUserLikesStore(
            Long campusIdx, Pageable pageable, String group, Long userIdx) {
        List<Map<String, Object>> stores =
                storeMapper.getUserLikesStore(
                        campusIdx,
                        pageable.getPageSize(),
                        pageable.getOffset(),
                        group,
                        userIdx,
                        "name",
                        null);
        List<LikedStoreGetRes> result = LikedStoreGetRes.of(stores);
        Long count =
                storeMapper.getUserLikesStoreCount(
                        campusIdx,
                        pageable.getPageSize(),
                        pageable.getOffset(),
                        group,
                        userIdx,
                        "name",
                        null);
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional
    public Boolean likesStore(Long storeIdx, Long userIdx) {
        User user =
                userRepository
                        .findById(userIdx)
                        .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
        Store store =
                storeRepository
                        .findById(storeIdx)
                        .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Optional<Like> isLikedStore = likeRepository.findByUserAndStore(user, store);
        if (isLikedStore.isPresent()) {
            likeRepository.delete(isLikedStore.get());
            return false;
        } else {
            Like like = Like.builder().store(store).user(user).build();
            likeRepository.save(like);
            return true;
        }
    }

    @Override
    public Page<StoreGetRes> getStoresKeyword(
            Long campusIdx, String keyword, Long userIdx, PageRequest pageRequest) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("universityIdx", campusIdx);
        parameter.put("keyword", keyword);
        parameter.put("userIdx", userIdx);
        parameter.put("limit", pageRequest.getPageSize());
        parameter.put("offset", pageRequest.getOffset());

        List<Map<String, Object>> storesKeyword = storeMapper.getStoresKeyword(parameter);
        Long storesKeywordCnt = storeMapper.getStoresKeywordCnt(parameter);

        List<StoreGetRes> result = StoreGetRes.of(storesKeyword);
        return new PageImpl<>(result, pageRequest, storesKeywordCnt);
    }

    @Override
    public Page<StoreGetReviewRes> getStoreReview(
            Long storeIdx, Long userIdx, PageRequest pageRequest) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("storeIdx", storeIdx);
        parameter.put("userIdx", userIdx);
        parameter.put("limit", pageRequest.getPageSize());
        parameter.put("offset", pageRequest.getOffset());

        List<Map<String, Object>> storeReview = storeMapper.getStoreReview(parameter);
        Long storeReviewCnt = storeMapper.getStoreReviewCnt(parameter);

        List<StoreGetReviewRes> result = StoreGetReviewRes.of(storeReview);

        return new PageImpl<>(result, pageRequest, storeReviewCnt);
    }

    @Override
    public Page<StoresGetReviews> getStoresReviews(
            PageRequest pageRequest, String order, String group, Integer grade, Long campusIdx) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("limit", pageRequest.getPageSize());
        parameter.put("offset", pageRequest.getOffset());
        parameter.put("order", order);
        parameter.put("group", group);
        parameter.put("grade", grade);
        parameter.put("campusIdx", campusIdx);

        List<Map<String, Object>> storesReviews = storeMapper.getStoresReviews(parameter);
        Long storesReviewsCnt = storeMapper.getStoresReviewsCnt(parameter);
        List<StoresGetReviews> result = StoresGetReviews.of(storesReviews);
        return new PageImpl<>(result, pageRequest, storesReviewsCnt);
    }
}
