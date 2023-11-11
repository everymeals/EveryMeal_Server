package everymeal.server.store.service;


import everymeal.server.store.controller.dto.response.StoreGetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface StoreService {

    Page<StoreGetRes> getStores(Long campusIdx, PageRequest of);
}
