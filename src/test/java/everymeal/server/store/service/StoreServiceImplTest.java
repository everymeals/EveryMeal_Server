package everymeal.server.store.service;

import static everymeal.server.store.entity.StoreSortVo.SORT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreMapper;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.store.repository.StoreRepositoryCustom;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.entity.Like;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.LikeRepository;
import everymeal.server.user.repository.UserRepository;
import everymeal.server.user.service.UserService;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

class StoreServiceImplTest extends IntegrationTestSupport {

    @Autowired private StoreServiceImpl storeService;
    @Autowired private StoreRepository storeRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private StoreMapper storeMapper;
    @Autowired private StoreRepositoryCustom storeRepositoryCustom;
    @Autowired private EntityManager entityManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private LikeRepository likeRepository;

    @DisplayName("캠퍼스 기준 식당 가져오기")
    @Transactional
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
        PageRequest pageRequest = PageRequest.of(offset, limit);

        List<Store> entity =
                List.of(
                        createEntity("store1", 3, university),
                        createEntity("store2", 2, university),
                        createEntity("store3", 1, university));
        storeRepository.saveAll(entity);
        storeRepository.flush();
        entityManager.clear();

        // when
        Page<StoreGetRes> stores =
                storeService.getStores(campusIdx, pageRequest, "etc", 1L, SORT_NAME, 1);

        // then
        assertThat(stores.getContent())
                .hasSize(3)
                .extracting("name")
                .containsExactly("store3", "store2", "store1");
    }

    @DisplayName("캠퍼스 기준 저장한 식당 가져오기")
    @Transactional
    @Test
    void getUserLikesStore() throws Exception {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());

        Long campusIdx = save.getIdx();
        int offset = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(offset, limit);

        List<Store> entity =
                List.of(
                        createEntity("store1", 3, save),
                        createEntity("store2", 2, save),
                        createEntity("store3", 1, save));

        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        UserEmailSingReq request =
                new UserEmailSingReq("연유크림", token, "12345", save.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());
        User userEntity = userRepository.findByNickname("연유크림").get();
        List<Like> likes =
                List.of(
                        createLikeEntity(entity.get(0), userEntity),
                        createLikeEntity(entity.get(1), userEntity),
                        createLikeEntity(entity.get(2), userEntity));
        storeRepository.saveAll(entity);
        storeRepository.flush();
        likeRepository.saveAll(likes);
        likeRepository.flush();
        entityManager.clear();

        // when
        Page<LikedStoreGetRes> stores =
                storeService.getUserLikesStore(campusIdx, pageRequest, "all", user);

        // then
        assertThat(stores.getContent())
                .hasSize(3)
                .extracting("name")
                .containsExactly("store3", "store2", "store1");
    }

    @DisplayName("가게 저장")
    @Test
    @Transactional
    void likesStore() {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());

        Store store = createEntity("store1", 3, save);
        storeRepository.saveAndFlush(store);
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        UserEmailSingReq request =
                new UserEmailSingReq("연유크림", token, "12345", save.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());
        User userEntity = userRepository.findByNickname("연유크림").get();

        // when
        var response = storeService.likesStore(store.getIdx(), user);
        var result = likeRepository.findByUserAndStore(userEntity, store);
        // then
        assertThat(result.isPresent());
    }

    @DisplayName("가게 저장 해제")
    @Test
    @Transactional
    void unlikesStore() {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());

        Store store = createEntity("store1", 3, save);
        storeRepository.saveAndFlush(store);
        String token = jwtUtil.generateEmailToken("test@gmail.com", "12345");

        UserEmailSingReq request =
                new UserEmailSingReq("연유크림", token, "12345", save.getIdx(), "imageKey");

        UserLoginRes userLoginRes = userService.signUp(request);

        AuthenticatedUser user =
                jwtUtil.getAuthenticateUserFromAccessToken(userLoginRes.accessToken());
        User userEntity = userRepository.findByNickname("연유크림").get();
        Like like = createLikeEntity(store, userEntity);
        likeRepository.saveAndFlush(like);
        // when
        var response = storeService.likesStore(store.getIdx(), user);
        var result = likeRepository.findByUserAndStore(userEntity, store);
        // then
        assertThat(!result.isPresent());
    }

    private Like createLikeEntity(Store store, User user) {
        return Like.builder().store(store).user(user).build();
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
                .recommendedCount(1)
                .university(university)
                .categoryDetail("기타")
                .build();
    }
}
