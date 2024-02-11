package everymeal.server.review.repository;

import static everymeal.server.meal.entity.QMeal.meal;
import static everymeal.server.meal.entity.QRestaurant.restaurant;
import static everymeal.server.review.entity.QImage.image;
import static everymeal.server.review.entity.QReview.review;
import static everymeal.server.review.entity.QReviewMark.reviewMark;
import static everymeal.server.review.entity.ReviewQueryParamVo.FILTER_TODAY;
import static everymeal.server.review.entity.ReviewQueryParamVo.SORT_GRADE;
import static everymeal.server.review.entity.ReviewQueryParamVo.SORT_LIKE;
import static everymeal.server.review.entity.ReviewQueryParamVo.SORT_RECENT;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import everymeal.server.review.dto.response.ReviewDto;
import everymeal.server.review.dto.response.ReviewDto.ReviewPagingVOWithCnt;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * ============================================================================================
     * 커서 기반 페이징으로 리뷰 목록 조회
     * =============================================================================================
     */
    @Override
    public ReviewPagingVOWithCnt getReview(ReviewDto.ReviewQueryParam queryParam) {
        var queryResult =
                jpaQueryFactory
                        .select(review)
                        .from(review)
                        .leftJoin(image)
                        .on(review.idx.eq(image.review.idx).and(image.isDeleted.eq(Boolean.FALSE)))
                        .leftJoin(reviewMark)
                        .on(review.idx.eq(reviewMark.review.idx))
                        .innerJoin(restaurant)
                        .on(
                                review.restaurant
                                        .idx
                                        .eq(restaurant.idx)
                                        .and(restaurant.idx.eq(queryParam.restaurantIdx())))
                        .where(
                                gtReviewIdx(queryParam.cursorIdx()),
                                isDeleted(),
                                eqToday(queryParam.filter()))
                        .groupBy(review.idx)
                        .orderBy(getOrderSpecifier(queryParam.order()))
                        .limit(queryParam.pageSize())
                        .fetch();
        var countResult =
                Objects.requireNonNull(
                                jpaQueryFactory
                                        .select(review.idx.count())
                                        .from(review)
                                        .leftJoin(image)
                                        .on(
                                                review.idx
                                                        .eq(image.review.idx)
                                                        .and(image.isDeleted.eq(Boolean.FALSE)))
                                        .leftJoin(reviewMark)
                                        .on(review.idx.eq(reviewMark.review.idx))
                                        .innerJoin(restaurant)
                                        .on(
                                                review.restaurant
                                                        .idx
                                                        .eq(restaurant.idx)
                                                        .and(
                                                                restaurant.idx.eq(
                                                                        queryParam
                                                                                .restaurantIdx())))
                                        .where(isDeleted(), eqToday(queryParam.filter()))
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

    private BooleanExpression eqToday(String filter) {
        return filter.equals(FILTER_TODAY) ? review.isTodayReview.eq(Boolean.TRUE) : null;
    }

    private BooleanExpression gtReviewIdx(Long cursorIdx) {
        if (cursorIdx == null) {
            return null;
        }
        return review.idx.gt(cursorIdx);
    }

    private OrderSpecifier getOrderSpecifier(String order) {

        if (order.equals(SORT_RECENT)) {
            return new OrderSpecifier(Order.DESC, review.createdAt);
        } else if (order.equals(SORT_GRADE)) {
            return new OrderSpecifier(Order.DESC, review.grade);
        } else if (order.equals(SORT_LIKE)) {
            return new OrderSpecifier(Order.DESC, review.reviewMarks.size());
        } else {
            return new OrderSpecifier(Order.DESC, review.createdAt);
        }
    }
}
