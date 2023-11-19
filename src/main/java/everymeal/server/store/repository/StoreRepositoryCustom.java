package everymeal.server.store.repository;


import everymeal.server.store.controller.dto.response.StoreGetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    Page<StoreGetRes> getStores(Long campusIdx, Pageable pageable, String group, Long userIdx,
        String order, Integer grade);
}
