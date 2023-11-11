package everymeal.server.review.service;

import static everymeal.server.global.exception.ExceptionList.MEAL_NOT_FOUND;
import static everymeal.server.global.exception.ExceptionList.REVIEW_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.repository.MealRepository;
import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewGetRes;
import everymeal.server.review.dto.ReviewPaging;
import everymeal.server.review.entity.Image;
import everymeal.server.review.entity.Review;
import everymeal.server.review.repository.ReviewRepository;
import everymeal.server.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final MealRepository mealRepository;
    private final ReviewRepository reviewRepository;
    private final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    @Transactional
    public Boolean createReview(ReviewCreateReq request, User user) {
        // (1) Meal 객체 조회
        Optional<Meal> meal = mealRepository.findById(request.mealIdx());
        if (meal.isEmpty()) {
            logger.info(MEAL_NOT_FOUND.MESSAGE, new ApplicationException(MEAL_NOT_FOUND));
            return false;
        }
        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        // (3) Entity 생성
        Review review =
                Review.builder()
                        .content(request.content())
                        .images(imageList)
                        .grade(request.grade())
                        .meal(meal.get())
                        .user(user)
                        .build();
        // (4) 저장
        reviewRepository.save(review);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateReview(ReviewCreateReq request, User user, Long reviewIdx) {
        // (1) 기존 리뷰 조회
        Review review =
                reviewRepository
                        .findById(reviewIdx)
                        .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));
        // (2) 이미지 주소 <> 이미지 객체 치환
        List<Image> imageList = getImageFromString(request.imageList());
        // (3) 기존 데이터 수정
        review.updateEntity(request.content(), request.grade(), imageList);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteReview(User user, Long reviewIdx) {
        // (1) 기존 리뷰 조회
        Review review =
                reviewRepository
                        .findByIdxAndUser(reviewIdx, user)
                        .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));
        // (2) 기존 데이터 삭제
        review.deleteEntity();
        return true;
    }

    public ReviewGetRes getReviewWithNoOffSetPaging(Long cursorIdx, Long mealIdx, int pageSize) {
        var result = reviewRepository.getReview(cursorIdx, mealIdx, pageSize);
        List<ReviewPaging> reviewPagingList = new ArrayList<>();
        for (Review vo : result.reviewList()) {
            List<String> strImgList = new ArrayList<>();
            vo.getImages().forEach(img -> strImgList.add(img.getImageUrl()));
            reviewPagingList.add(
                    new ReviewPaging(
                            vo.getIdx(),
                            vo.getMeal().getRestaurant().getName(),
                            vo.getMeal().getMealType().name(),
                            vo.getGrade(),
                            vo.getContent(),
                            strImgList,
                            vo.getReviewMarks().size()));
        }

        return new ReviewGetRes(result.reviewTotalCnt(), reviewPagingList);
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
