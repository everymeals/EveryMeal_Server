package everymeal.server.store.service;


import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public Page<StoreGetRes> getStores(Long campusIdx, PageRequest pageRequest) {
        Page<Store> stores = storeRepository.findByUniversityIdx(campusIdx, pageRequest);
        return StoreGetRes.of(stores);
    }
}
