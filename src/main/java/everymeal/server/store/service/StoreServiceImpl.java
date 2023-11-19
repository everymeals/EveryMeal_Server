package everymeal.server.store.service;


import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.store.repository.StoreRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreRepositoryCustom storeRepositoryCustom;

    @Override
    public Page<StoreGetRes> getStores(
        Long campusIdx, Pageable pageable, String group, Long userIdx, String order) {
        return storeRepositoryCustom.getStores(campusIdx, pageable, group, userIdx, order);
    }
}
