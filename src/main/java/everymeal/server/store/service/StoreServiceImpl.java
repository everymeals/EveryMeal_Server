package everymeal.server.store.service;

import static everymeal.server.global.exception.ExceptionList.STORE_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.review.repository.ImageRepository;
import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
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
    private final StoreRepository storeRepository;
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
        Store store = storeRepository.findById(storeIdx).orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        boolean isLike = false;
        if (userIdx != null) {
            isLike = likeRepository
                .findByUserIdxAndStoreIdx(userIdx, storeIdx).isPresent();
        }
        return StoreGetRes.of(store, isLike);
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
}
