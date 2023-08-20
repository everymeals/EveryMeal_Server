package everymeal.server.store.service;


import everymeal.server.global.util.KakaoWebClientUtil;
import everymeal.server.meal.repository.AreaRepository;
import everymeal.server.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final AreaRepository areaRepository;
    private final KakaoWebClientUtil kakaoWebClientUtil;

    @Override
    public List<String> getStoresByArea(String area) {
        //        String searchKeyword = "명지대학교 서울캠퍼스 커피";
        //        Map block =
        //                kakaoWebClientUtil
        //                        .getWebClient()
        //                        .get()
        //                        .uri("/v2/local/search/keyword?query={area}", searchKeyword)
        //                        .retrieve()
        //                        .bodyToMono(Map.class)
        //                        .block();
        //        List<Store> all = storeRepository.findAll();
        return null;
    }
}
