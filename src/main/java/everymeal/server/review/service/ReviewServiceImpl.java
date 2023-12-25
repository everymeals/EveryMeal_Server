package everymeal.server.review.service;

import static everymeal.server.global.exception.ExceptionList.RESTAURANT_NOT_FOUND;
import static everymeal.server.global.exception.ExceptionList.REVIEW_ALREADY_MARKED;
import static everymeal.server.global.exception.ExceptionList.REVIEW_MARK_NOT_FOUND;
import static everymeal.server.global.exception.ExceptionList.REVIEW_NOT_FOUND;
import static everymeal.server.global.exception.ExceptionList.REVIEW_UNAUTHORIZED;
import static everymeal.server.global.exception.ExceptionList.USER_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewDto;
import everymeal.server.review.dto.ReviewDto.ReviewTodayGetRes;
import everymeal.server.review.dto.ReviewGetRes;
import everymeal.server.review.dto.ReviewPaging;
import everymeal.server.review.entity.Image;
import everymeal.server.review.entity.Review;
import everymeal.server.review.repository.ReviewMapper;
import everymeal.server.review.repository.ReviewMarkRepository;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewMarkRepository reviewMarkRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public Long createReview(ReviewCreateReq request, Long userIdx) {
        // (1) restaurant 객체 조회
        Restaurant restaurant =
                restaurantRepository
                        .findById(request.restaurantIdx())
                        .orElseThrow(() -> new ApplicationException(RESTAURANT_NOT_FOUND));

        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        User user =
                userRepository
                        .findById(userIdx)
                        .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

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
        Review savedReview = reviewRepository.save(review);

        restaurant.addGrade(request.grade());
        return savedReview.getIdx();
    }

    @Override
    @Transactional
    public Long updateReview(ReviewCreateReq request, Long userIdx, Long reviewIdx) {
        // (1) 기존 리뷰 조회
        Review review =
                reviewRepository
                        .findById(reviewIdx)
                        .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));
        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        User user =
                userRepository
                        .findById(userIdx)
                        .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
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
        User user =
                userRepository
                        .findById(userIdx)
                        .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Review review =
                reviewRepository
                        .findByIdxAndUser(reviewIdx, user)
                        .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));
        // (2) 기존 데이터 삭제
        review.getRestaurant().removeGrade(review.getGrade());
        review.deleteEntity();
        return true;
    }

    public ReviewGetRes getReviewWithNoOffSetPaging(
            Long cursorIdx, Long restaurantIdx, int pageSize) {
        var result = reviewRepository.getReview(cursorIdx, restaurantIdx, pageSize);
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
                            vo.getReviewMarks().size()));
        }

        return new ReviewGetRes(result.reviewTotalCnt(), reviewPagingList);
    }

    @Override
    @Transactional
    public Boolean markReview(Long reviewIdx, boolean isLike, Long userIdx) {
        // (1) 기존 리뷰 조회
        Review review =
                reviewRepository
                        .findById(reviewIdx)
                        .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));
        User user =
                userRepository
                        .findById(userIdx)
                        .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        // (2) 기존 데이터 수정
        if (isLike) {
            reviewMarkRepository
                    .findByReviewIdxAndUserIdx(reviewIdx, userIdx)
                    .ifPresentOrElse(
                            reviewMark -> {
                                throw new ApplicationException(REVIEW_ALREADY_MARKED);
                            },
                            () -> {
                                review.addMark(user);
                                reviewRepository.saveAndFlush(review);
                            });
        } else {
            reviewMarkRepository
                    .findByReviewIdxAndUserIdx(reviewIdx, userIdx)
                    .ifPresentOrElse(
                            reviewMark -> {
                                review.removeMark(user);
                                reviewMarkRepository.delete(reviewMark);
                            },
                            () -> {
                                throw new ApplicationException(REVIEW_MARK_NOT_FOUND);
                            });
        }
        return true;
    }

    @Override
    public ReviewTodayGetRes getTodayReview(Long restaurantIdx, String offeredAt) {
        return ReviewDto.of(reviewMapper.findTodayReview(restaurantIdx,  offeredAt));
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
