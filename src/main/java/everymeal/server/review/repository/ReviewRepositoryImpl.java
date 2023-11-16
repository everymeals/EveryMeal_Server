package everymeal.server.review.repository;

import static everymeal.server.meal.entity.QMeal.meal;
import static everymeal.server.meal.entity.QRestaurant.restaurant;
import static everymeal.server.review.entity.QImage.image;
import static everymeal.server.review.entity.QReview.review;
import static everymeal.server.review.entity.QReviewMark.reviewMark;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import everymeal.server.review.dto.QReviewPagingVO;
import everymeal.server.review.dto.ReviewPagingVOWithCnt;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * ============================================================================================
     * 커서 기반 페이징으로 리뷰 목록 조회 VO 형식으로 필요 데이터만 받기
     * =============================================================================================
     */
    @Override
    public ReviewPagingVOWithCnt getReviewWithNoOffSetPaging(
            Long cursorIdx, Long mealIdx, int pageSize) {
        var queryResult =
                jpaQueryFactory
                        .select(
                                new QReviewPagingVO(
                                        review.idx.as("reviewIdx"),
                                        restaurant.name.as("restaurantName"),
                                        meal.mealType.stringValue().as("mealType"),
                                        review.grade,
                                        review.content,
                                        review.images.as("imageList"),
                                        review.reviewMarks.size().as("reviewMarksCnt")))
                        .from(review)
                        .leftJoin(review.images, image)
                        .leftJoin(review.reviewMarks, reviewMark)
                        .leftJoin(review.meal, meal)
                        .leftJoin(meal.restaurant, restaurant)
                        .where(gtReviewIdx(cursorIdx), eqMealIdx(mealIdx), isDeleted())
                        .groupBy(review.idx)
                        .orderBy(review.idx.desc())
                        .limit(pageSize)
                        .fetch();
        var countResult =
                Objects.requireNonNull(
                                jpaQueryFactory
                                        .select(review.idx.count())
                                        .from(review)
                                        .where(isDeleted())
                                        .fetchOne())
                        .intValue();
        // return new ReviewPagingVOWithCnt(countResult, queryResult);
        return null;
    }
    /**
     * ============================================================================================
     * 커서 기반 페이징으로 리뷰 목록 조회
     * =============================================================================================
     */
    @Override
    public ReviewPagingVOWithCnt getReview(Long cursorIdx, Long mealIdx, int pageSize) {
        var queryResult =
                jpaQueryFactory
                        .select(review)
                        .from(review)
                        .leftJoin(review.images, image)
                        .leftJoin(review.reviewMarks, reviewMark)
                        .leftJoin(review.meal, meal)
                        .leftJoin(meal.restaurant, restaurant)
                        .where(gtReviewIdx(cursorIdx), eqMealIdx(mealIdx), isDeleted())
                        .groupBy(review.idx)
                        .orderBy(review.idx.desc())
                        .limit(pageSize)
                        .fetch();
        var countResult =
                Objects.requireNonNull(
                                jpaQueryFactory
                                        .select(review.idx.count())
                                        .from(review)
                                        .where(isDeleted())
                                        .fetchOne())
                        .intValue();
        return new ReviewPagingVOWithCnt(countResult, queryResult);
    }
    /**
     * ============================================================================================
     * PRIVATE FUNCTION --BooleanExpression
     * =============================================================================================
     */
    private BooleanExpression isDeleted() {
        return review.isDeleted.eq(Boolean.FALSE);
    }

    private BooleanExpression eqMealIdx(Long mealIdx) {
        return mealIdx == null ? null : meal.idx.eq(mealIdx);
    }

    private BooleanExpression gtReviewIdx(Long cursorIdx) {
        if (cursorIdx == null) {
            return null;
        }
        return review.idx.gt(cursorIdx);
    }
}