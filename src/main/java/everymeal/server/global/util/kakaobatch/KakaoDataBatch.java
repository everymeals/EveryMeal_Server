package everymeal.server.global.util.kakaobatch;


import com.fasterxml.jackson.databind.ObjectMapper;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class KakaoDataBatch {

    private final StoreRepository storeRepository;
    private final KakaoWebClientUtil kakaoWebClientUtil;
    private final UniversityRepository universityRepository;

    @Transactional
    public Map getStores(String keyword, String universityName, String campus) {
        int page = 1;
        boolean isEnd = false;
        String x = "127.091621159803";
        String y = "37.6273815936787";
        List<University> byNameAndCampusNameAndIsDeletedFalse =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse(
                        universityName, campus);
        WebClient webClient = kakaoWebClientUtil.getWebClient();
        University university = byNameAndCampusNameAndIsDeletedFalse.get(0);
        while (!isEnd) {
            int finalPage = page;
            Map block =
                    webClient
                            .get()
                            .uri(
                                    uriBuilder ->
                                            uriBuilder
                                                    .path("/v2/local/search/keyword.json")
                                                    .queryParam("query", keyword)
                                                    .queryParam("category_group_code", "FD6")
                                                    .queryParam("x", x)
                                                    .queryParam("y", y)
                                                    .queryParam("radius", "3000")
                                                    .queryParam("size", "15")
                                                    .queryParam("page", String.valueOf(finalPage))
                                                    .queryParam("sort", "distance")
                                                    .build())
                            .retrieve()
                            .bodyToMono(Map.class)
                            .block();
            Map meta = (Map) block.get("meta");
            isEnd = (boolean) meta.get("is_end");
            List<KakaoStoreVO> documents =
                    ((List<Map<String, Object>>) block.get("documents"))
                            .stream()
                                    .map(
                                            data -> {
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                return objectMapper.convertValue(
                                                        data, KakaoStoreVO.class);
                                            })
                                    .collect(Collectors.toList());
            for (KakaoStoreVO kakaoStoreVO : documents) {
                Optional<Store> byKakaoId = storeRepository.findByKakaoId(kakaoStoreVO.getId());
                if (byKakaoId.isEmpty()) {
                    Store kakaoStore = kakaoStoreVO.of(university);
                    storeRepository.save(kakaoStore);
                }
            }
            page++;
        }

        page = 1;
        isEnd = false;
        while (!isEnd) {
            int finalPage = page;
            Map block =
                    webClient
                            .get()
                            .uri(
                                    uriBuilder ->
                                            uriBuilder
                                                    .path("/v2/local/search/keyword.json")
                                                    .queryParam("query", keyword)
                                                    .queryParam("category_group_code", "CE7")
                                                    .queryParam("x", x)
                                                    .queryParam("y", y)
                                                    .queryParam("radius", "3000")
                                                    .queryParam("size", "15")
                                                    .queryParam("page", String.valueOf(finalPage))
                                                    .queryParam("sort", "distance")
                                                    .build())
                            .retrieve()
                            .bodyToMono(Map.class)
                            .block();
            Map meta = (Map) block.get("meta");
            isEnd = (boolean) meta.get("is_end");
            List<KakaoStoreVO> documents =
                    ((List<Map<String, Object>>) block.get("documents"))
                            .stream()
                                    .map(
                                            data -> {
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                return objectMapper.convertValue(
                                                        data, KakaoStoreVO.class);
                                            })
                                    .collect(Collectors.toList());
            for (KakaoStoreVO kakaoStoreVO : documents) {
                Optional<Store> byKakaoId = storeRepository.findByKakaoId(kakaoStoreVO.getId());
                if (byKakaoId.isEmpty()) {
                    Store kakaoStore = kakaoStoreVO.of(university);
                    storeRepository.save(kakaoStore);
                }
            }
            page++;
        }

        return null;
    }
}
