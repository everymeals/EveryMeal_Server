package everymeal.server.store.service;


import everymeal.server.global.util.KakaoWebClientUtil;
import everymeal.server.meal.repository.AreaRepository;
import everymeal.server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final AreaRepository areaRepository;
    private final KakaoWebClientUtil kakaoWebClientUtil;
}
