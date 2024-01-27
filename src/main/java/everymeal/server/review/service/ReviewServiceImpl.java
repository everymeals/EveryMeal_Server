package everymeal.server.review.service;

import static everymeal.server.global.exception.ExceptionList.REVIEW_ALREADY_MARKED;
import static everymeal.server.global.exception.ExceptionList.REVIEW_MARK_NOT_FOUND;
import static everymeal.server.global.exception.ExceptionList.REVIEW_UNAUTHORIZED;
import static everymeal.server.global.exception.ExceptionList.STORE_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.util.TimeFormatUtil;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.service.RestaurantCommServiceImpl;
import everymeal.server.review.dto.request.ReviewCreateReq;
import everymeal.server.review.dto.response.ReviewDto;
import everymeal.server.review.dto.response.ReviewDto.ReviewGetRes;
import everymeal.server.review.dto.response.ReviewDto.ReviewPaging;
import everymeal.server.review.dto.response.ReviewDto.ReviewTodayGetRes;
import everymeal.server.review.entity.Image;
import everymeal.server.review.entity.Review;
import everymeal.server.store.entity.Store;
import everymeal.server.store.repository.StoreRepository;
import everymeal.server.user.entity.User;
import everymeal.server.user.service.UserCommServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantCommServiceImpl restaurantCommServiceImpl;
    private final UserCommServiceImpl userCommServiceImpl;
    private final ReviewCommServiceImpl reviewCommServiceImpl;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public Long createReview(ReviewCreateReq request, Long userIdx) {
        // (1) restaurant 객체 조회
        Restaurant restaurant = restaurantCommServiceImpl.getRestaurantEntity(request.idx());

        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        User user = userCommServiceImpl.getUserEntity(userIdx);

        // (3) Entity 생성 ( 사진리뷰인지 분기 처리 )
        Review review =
                (request.content() == null && request.grade() == 0)
                        ? Review.builder()
                                .images(imageList)
                                .restaurant(restaurant)
                                .user(user)
                                .build()
                        : Review.builder()
                                .content(request.content())
                                .images(imageList)
                                .restaurant(restaurant)
                                .grade(request.grade())
                                .user(user)
                                .build();

        if (request.isTodayReview()) {
            review.updateTodayReview(true);
        }
        // (4) 저장
        Review savedReview = reviewCommServiceImpl.save(review);

        restaurant.addGrade(request.grade());
        return savedReview.getIdx();
    }

    @Override
    @Transactional
    public Long updateReview(ReviewCreateReq request, Long userIdx, Long reviewIdx) {
        // (1) 기존 리뷰 조회
        Review review = reviewCommServiceImpl.getReviewEntity(reviewIdx);

        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        User user = userCommServiceImpl.getUserEntity(userIdx);
        if (review.getUser() != user) {
            throw new ApplicationException(REVIEW_UNAUTHORIZED);
        }
        // (3) 기존 데이터 수정
        review.getRestaurant().changeGrade(review.getGrade(), request.grade());

        review.updateEntity(request.content(), request.grade(), imageList, request.isTodayReview());

        return review.getIdx();
    }

    @Override
    @Transactional
    public Boolean deleteReview(Long userIdx, Long reviewIdx) {
        // (1) 기존 리뷰 조회
        User user = userCommServiceImpl.getUserEntity(userIdx);
        Review review = reviewCommServiceImpl.getReviewEntity(reviewIdx, user);
        // (2) 기존 데이터 삭제
        review.getRestaurant().removeGrade(review.getGrade());
        review.deleteEntity();

        reviewCommServiceImpl
                .getReviewMarkOptionalEntity(reviewIdx, userIdx)
                .ifPresent(
                        reviewMark -> {
                            review.removeMark(user);
                            reviewCommServiceImpl.deleteReviewMark(reviewMark);
                        });

        return true;
    }

    public ReviewGetRes getReviewWithNoOffSetPaging(ReviewDto.ReviewQueryParam queryParam) {
        var result = reviewCommServiceImpl.getReviewPagingVOWithCnt(queryParam);
        List<ReviewPaging> reviewPagingList = new ArrayList<>();
        for (Review vo : result.reviewList()) {
            List<String> strImgList = new ArrayList<>();
            vo.getImages().forEach(img -> strImgList.add(img.getImageUrl()));
            reviewPagingList.add(
                    new ReviewPaging(
                            vo.getIdx(),
                            vo.getRestaurant().getName(),
                            vo.getUser().getNickname(),
                            vo.getUser().getProfileImgUrl(),
                            vo.isTodayReview(),
                            vo.getGrade(),
                            vo.getContent(),
                            strImgList,
                            vo.getReviewMarks().size(),
                            TimeFormatUtil.getTimeFormat(vo.getCreatedAt())));
        }

        return new ReviewGetRes(result.reviewTotalCnt(), reviewPagingList);
    }

    @Override
    @Transactional
    public Boolean markReview(Long reviewIdx, boolean isLike, Long userIdx) {
        // (1) 기존 리뷰 조회
        Review review = reviewCommServiceImpl.getReviewEntity(reviewIdx);
        User user = userCommServiceImpl.getUserEntity(userIdx);
        // (2) 기존 데이터 수정
        if (isLike) {
            reviewCommServiceImpl
                    .getReviewMarkOptionalEntity(reviewIdx, userIdx)
                    .ifPresentOrElse(
                            reviewMark -> {
                                throw new ApplicationException(REVIEW_ALREADY_MARKED);
                            },
                            () -> {
                                review.addMark(user);
                                reviewCommServiceImpl.saveAndFlush(review);
                            });
        } else {
            reviewCommServiceImpl
                    .getReviewMarkOptionalEntity(reviewIdx, userIdx)
                    .ifPresentOrElse(
                            reviewMark -> {
                                review.removeMark(user);
                                reviewCommServiceImpl.deleteReviewMark(reviewMark);
                            },
                            () -> {
                                throw new ApplicationException(REVIEW_MARK_NOT_FOUND);
                            });
        }
        return true;
    }

    @Override
    public ReviewTodayGetRes getTodayReview(Long restaurantIdx, String offeredAt) {
        return ReviewDto.of(reviewCommServiceImpl.getTodayReviewEntityFromMapper(restaurantIdx, offeredAt));
    }

    @Override
    @Transactional
    public Long createReviewByStore(ReviewCreateReq request, Long userIdx) {
        // (1) restaurant 객체 조회
        Store store =
                storeRepository
                        .findById(request.idx())
                        .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        User user = userCommServiceImpl.getUserEntity(userIdx);

        // (3) Entity 생성 ( 사진리뷰인지 분기 처리 )
        Review review =
                (request.content() == null && request.grade() == 0)
                        ? Review.builder().images(imageList).store(store).user(user).build()
                        : Review.builder()
                                .content(request.content())
                                .images(imageList)
                                .store(store)
                                .grade(request.grade())
                                .user(user)
                                .build();

        // (4) 저장
        Review savedReview = reviewCommServiceImpl.save(review);

        store.addGrade(request.grade());
        return savedReview.getIdx();
    }

    /**
     * ============================================================================================
     * PRIVATE FUNCTION
     * =============================================================================================
     */
    private List<Image> getImageFromString(List<String> reqImgList) {
        List<Image> imageList = new ArrayList<>();
        if (reqImgList.size() > 0) {
            reqImgList.forEach(img -> imageList.add(Image.builder().imageUrl(img).build()));
        }
        return imageList;
    }
}
