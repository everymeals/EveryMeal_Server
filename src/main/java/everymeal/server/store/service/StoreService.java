package everymeal.server.store.service;


import everymeal.server.global.dto.response.Cursor;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface StoreService {

    Cursor<StoreGetRes> getStores(Long campusIdx, PageRequest of, Long cursorId);
}
