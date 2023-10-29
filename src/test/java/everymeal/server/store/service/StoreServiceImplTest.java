package everymeal.server.store.service;

import static org.assertj.core.api.Assertions.assertThat;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class StoreServiceImplTest extends IntegrationTestSupport {

    @Autowired private StoreServiceImpl storeService;
    @Autowired private StoreRepository storeRepository;
    @Autowired private UniversityRepository universityRepository;

    @AfterEach
    void tearDown() {
        storeRepository.deleteAllInBatch();
        universityRepository.deleteAllInBatch();
    }

    @DisplayName("캠퍼스 기준 식당 가져오기")
    @Test
    void getStores() throws Exception {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse("서울대학교", "관악캠퍼스");
        University university = universities.get(0);

        Long campusIdx = university.getIdx();
        int offset = 0;
        int limit = 10;
        String orderBy = "distance";
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(orderBy).ascending());

        List<Store> entity =
                List.of(
                        createEntity("store1", 3, university),
                        createEntity("store2", 2, university),
                        createEntity("store3", 1, university));
        storeRepository.saveAll(entity);

        // when
        Page<StoreGetRes> stores = storeService.getStores(campusIdx, pageRequest);

        // then
        assertThat(stores.getContent())
                .hasSize(3)
                .extracting("name")
                .containsExactly("store3", "store2", "store1");
    }

    private Store createEntity(String name, int distance, University university) {
        return Store.builder()
                .name(name)
                .address("address")
                .categoryGroup("categoryGroup")
                .category("category")
                .kakaoId("kakaoId")
                .phone("phone")
                .distance(distance)
                .url("url")
                .roadAddress("roadAddress")
                .x("x")
                .y("y")
                .grade(1.0)
                .reviewCount(1)
                .recommendedCnt(1)
                .university(university)
                .build();
    }
}
