package everymeal.server.store.service;

import static everymeal.server.store.entity.StoreSortVo.SORT_NAME;
import static everymeal.server.store.entity.StoreSortVo.SORT_REVIEWMARKCOUNT;
import static org.assertj.core.api.Assertions.assertThat;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.review.entity.Image;
import everymeal.server.review.entity.Review;
import everymeal.server.review.repository.ImageRepository;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetReviewRes;
import everymeal.server.store.controller.dto.response.StoresGetReviews;
import everymeal.server.store.entity.GradeStatistics;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreMapper;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.store.repository.StoreRepositoryCustom;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import everymeal.server.user.entity.Like;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.LikeRepository;
import everymeal.server.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
    @Autowired private UserRepository userRepository;
    @Autowired private LikeRepository likeRepository;
    @Autowired private ImageRepository imageRepository;
    @Autowired private ReviewRepository reviewRepository;

    @AfterEach
    void tearDown() {
        likeRepository.deleteAll();
        storeRepository.deleteAll();
        universityRepository.deleteAll();
        userRepository.deleteAll();
        //        imageRepository.deleteAll();
        //        reviewRepository.deleteAll();
    }

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
                        createEntity("store1", 3, university, "기타"),
                        createEntity("store2", 2, university, "기타"),
                        createEntity("store3", 1, university, "기타"));
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
                        createEntity("store1", 3, save, "기타"),
                        createEntity("store2", 2, save, "기타"),
                        createEntity("store3", 1, save, "기타"));

        User userEntity = userRepository.save(getUser(save, 1));
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
                storeService.getUserLikesStore(campusIdx, pageRequest, "all", userEntity.getIdx());
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

        Store store = createEntity("store1", 3, save, "기타");
        storeRepository.saveAndFlush(store);
        User userEntity = userRepository.save(getUser(save, 1));

        // when
        var response = storeService.likesStore(store.getIdx(), userEntity.getIdx());
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

        Store store = createEntity("store1", 3, save, "기타");
        storeRepository.saveAndFlush(store);
        User userEntity = userRepository.save(getUser(save, 1));
        Like like = createLikeEntity(store, userEntity);
        likeRepository.saveAndFlush(like);
        // when
        var response = storeService.likesStore(store.getIdx(), userEntity.getIdx());
        var result = likeRepository.findByUserAndStore(userEntity, store);
        // then
        assertThat(!result.isPresent());
    }

    @DisplayName("가게 키워드 검색 - 이름")
    @Test
    @Transactional
    void searchStore() {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse("서울대학교", "관악캠퍼스");
        University university = universities.get(0);

        Long campusIdx = university.getIdx();

        List<Store> entity =
                List.of(
                        createEntity("치킨", 3, university, "기타"),
                        createEntity("떡볶이", 2, university, "기타"),
                        createEntity("BBQ 치킨", 1, university, "기타"));
        storeRepository.saveAll(entity);
        storeRepository.flush();
        entityManager.clear();

        // when

        Page<StoreGetRes> stores =
                storeService.getStoresKeyword(campusIdx, "치킨", null, PageRequest.of(0, 10));

        // then
        assertThat(stores.getContent())
                .hasSize(2)
                .extracting("name")
                .containsExactly("치킨", "BBQ 치킨");
    }

    @DisplayName("가게 키워드 검색 - 카테고리 디테일")
    @Test
    @Transactional
    void searchStoreForCategory() {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse("서울대학교", "관악캠퍼스");
        University university = universities.get(0);

        Long campusIdx = university.getIdx();

        List<Store> entity =
                List.of(
                        createEntity("샌드위치", 3, university, "치킨"),
                        createEntity("BBQ", 2, university, "치킨"),
                        createEntity("카페", 1, university, "기타"));
        storeRepository.saveAll(entity);
        storeRepository.flush();
        entityManager.clear();

        // when

        Page<StoreGetRes> stores =
                storeService.getStoresKeyword(campusIdx, "치킨", null, PageRequest.of(0, 10));

        // then
        assertThat(stores.getContent())
                .hasSize(2)
                .extracting("name")
                .containsExactly("샌드위치", "BBQ");
    }

    @DisplayName("가게 상세 조회")
    @Test
    @Transactional
    void getStoreDetail() {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse("서울대학교", "관악캠퍼스");
        University university = universities.get(0);

        Store store = createEntity("샌드위치", 3, university, "치킨");
        storeRepository.save(store);

        User user = getUser(university, 1);
        userRepository.save(user);
        Review review = createReviewEntity(store, user);
        reviewRepository.save(review);

        List<Image> images =
                List.of(
                        createImageEntity("1", review),
                        createImageEntity("2", review),
                        createImageEntity("3", review),
                        createImageEntity("4", review),
                        createImageEntity("5", review),
                        createImageEntity("6", review));
        imageRepository.saveAll(images);

        storeRepository.flush();
        userRepository.flush();
        reviewRepository.flush();
        imageRepository.flush();
        entityManager.clear();

        // when
        StoreGetRes detail = storeService.getStore(store.getIdx(), null);

        // then
        assertThat(detail.images()).hasSize(5);
        assertThat(detail.name()).isEqualTo("샌드위치");
    }

    @DisplayName("가게 리뷰 조회")
    @Test
    @Transactional
    void getStoreReviews() {
        // given
        University save =
                universityRepository.save(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse("서울대학교", "관악캠퍼스");
        University university = universities.get(0);

        Store store = createEntity("샌드위치", 3, university, "치킨");
        storeRepository.save(store);

        User user = getUser(university, 1);
        userRepository.save(user);

        Review review = createReviewEntity(store, user);
        reviewRepository.save(review);

        List<Image> images =
                List.of(
                        createImageEntity("1", review),
                        createImageEntity("2", review),
                        createImageEntity("3", review),
                        createImageEntity("4", review),
                        createImageEntity("5", review),
                        createImageEntity("6", review));
        imageRepository.saveAll(images);

        entityManager.clear();

        // when
        Page<StoreGetReviewRes> storeReview =
                storeService.getStoreReview(store.getIdx(), null, PageRequest.of(0, 10));

        // then
        assertThat(storeReview.getContent()).hasSize(1);
        assertThat(storeReview.getContent().get(0).content()).isEqualTo("content");
    }

    @DisplayName("가게 리뷰 조회 - 페이징")
    @Test
    @Transactional
    void getStoreReviewsPaging() {
        // given
        University save =
                universityRepository.saveAndFlush(
                        University.builder().name("서울대학교").campusName("관악캠퍼스").build());
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse("서울대학교", "관악캠퍼스");
        University university = universities.get(0);

        Store store = createEntity("샌드위치", 3, university, "치킨");
        storeRepository.saveAndFlush(store);

        User user = getUser(university, 1);
        userRepository.saveAndFlush(user);

        Review review = createReviewEntity(store, user);
        reviewRepository.saveAndFlush(review);

        List<Image> images =
                List.of(
                        createImageEntity("1", review),
                        createImageEntity("2", review),
                        createImageEntity("3", review),
                        createImageEntity("4", review),
                        createImageEntity("5", review),
                        createImageEntity("6", review));
        imageRepository.saveAllAndFlush(images);

        entityManager.flush();
        entityManager.clear();

        // when
        Page<StoresGetReviews> storesReviews =
                storeService.getStoresReviews(
                        PageRequest.of(0, 10),
                        SORT_REVIEWMARKCOUNT,
                        "all",
                        null,
                        university.getIdx());

        // then
        assertThat(storesReviews.getContent()).hasSize(1);
        assertThat(storesReviews.getContent().get(0).content()).isEqualTo("content");
    }

    private Image createImageEntity(String urlNumber, Review review) {
        return Image.builder().imageUrl("url" + urlNumber).review(review).build();
    }

    private Review createReviewEntity(Store store, User user) {
        return Review.builder().store(store).user(user).content("content").grade(0).build();
    }

    private Like createLikeEntity(Store store, User user) {
        return Like.builder().store(store).user(user).build();
    }

    private Store createEntity(
            String name, int distance, University university, String categoryDetail) {
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
                .gradeStatistics(new GradeStatistics())
                .university(university)
                .categoryDetail(categoryDetail)
                .build();
    }

    private User getUser(University university, int uniqueIdx) {
        return User.builder()
                .email(uniqueIdx + "test@gmail.com")
                .university(university)
                .nickname(uniqueIdx + "띵랑이")
                .profileImgUrl("img.url")
                .build();
    }
}
