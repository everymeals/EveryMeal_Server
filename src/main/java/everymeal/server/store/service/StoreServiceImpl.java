package everymeal.server.store.service;


import everymeal.server.global.dto.response.Cursor;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public Cursor<StoreGetRes> getStores(Long universityIdx, PageRequest pageRequest,
        Long cursorId) {
        List<Store> stores = getStore(universityIdx, pageRequest, cursorId);
        Long lastIdx = stores.isEmpty() ? null : stores.get(stores.size() - 1).getIdx();
        return new Cursor<>(StoreGetRes.of(stores),
            hashNext(universityIdx, lastIdx));
    }

    private List<Store> getStore(Long universityIdx, Pageable pageable, Long cursorId) {
        return cursorId == null ?
            storeRepository.findByUniversityIdxOrderByIdxDesc(universityIdx, pageable) :
            storeRepository.findByUniversityIdxAndIdxLessThanOrderByIdxDesc(universityIdx, cursorId, pageable);
    }

    private Boolean hashNext(Long universityIdx, Long cursorId) {
        return storeRepository.existsByIdxLessThanAndUniversityIdx(cursorId, universityIdx);
    }
}
