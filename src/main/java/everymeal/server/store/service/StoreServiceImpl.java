package everymeal.server.store.service;


import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.repository.StoreMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreMapper storeMapper;

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
}
