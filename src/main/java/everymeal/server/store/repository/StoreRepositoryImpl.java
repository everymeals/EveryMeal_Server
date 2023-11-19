package everymeal.server.store.repository;


import static everymeal.server.store.entity.QStore.store;
import static everymeal.server.store.entity.StoreSortVo.SORT_DISTANCE;
import static everymeal.server.store.entity.StoreSortVo.SORT_GRADE;
import static everymeal.server.store.entity.StoreSortVo.SORT_NAME;
import static everymeal.server.store.entity.StoreSortVo.SORT_RECENT;
import static everymeal.server.store.entity.StoreSortVo.SORT_RECOMMENDEDCNOUNT;
import static everymeal.server.store.entity.StoreSortVo.SORT_REVIEWCOUNT;
import static everymeal.server.user.entity.QLike.like;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Page<StoreGetRes> getStores(
        Long universityIdx, Pageable pageable, String group, Long userIdx, String order) {
        List<StoreGetRes> fetch = query
            .select(
                Projections.constructor(
                    StoreGetRes.class,
                    store.idx,
                    store.name,
                    store.address,
                    store.phone,
                    store.categoryDetail,
                    store.distance,
                    store.grade,
                    store.reviewCount,
                    store.recommendedCount,
                    getStoreLikeCheck(userIdx)
                )
            )
            .from(store)
            .where(store.university.idx.eq(universityIdx)
                .and(store.isDeleted.eq(false))
                .and(getWhereCase(group))
            )
            .orderBy(getOrderCase(order))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = query
            .select(store.count())
            .from(store)
            .where(store.university.idx.eq(universityIdx)
                .and(store.isDeleted.eq(false))
            ).fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    private BooleanBuilder getWhereCase(String group) {
        BooleanBuilder whereClause = new BooleanBuilder();
        BooleanExpression expression = switch (group){
            case "recommend" ->  store.recommendedCount.gt(0);
            case "restaurant" -> store.categoryDetail.eq("한식")
                    .or(store.categoryDetail.eq("중식")
                    .or(store.categoryDetail.eq("일식"))
                    .or(store.categoryDetail.eq("양식")));
            case "cafe" -> store.categoryDetail.eq("카페");
            case "bar" -> store.categoryDetail.eq("술집");
            default -> store.categoryDetail.eq("기타")
                .or(store.categoryDetail.eq("패스트푸드")
                    .or(store.categoryDetail.eq("분식")));
        };
        return whereClause.and(expression);
    }

    private OrderSpecifier<?> getOrderCase(String order) {
        return switch (order) {
            case SORT_NAME -> store.name.asc();
            case SORT_DISTANCE -> store.distance.asc();
            case SORT_RECOMMENDEDCNOUNT -> store.recommendedCount.desc();
            case SORT_REVIEWCOUNT -> store.reviewCount.desc();
            case SORT_GRADE -> store.grade.desc();
            case SORT_RECENT -> store.createdAt.desc();
            default -> store.idx.desc();
        };
    }

    private JPQLQuery<Boolean> getStoreLikeCheck(Long userIdx) {
        if (userIdx == null) {
            return JPAExpressions.select(Expressions.constant(false));
        }else{
            return JPAExpressions.select(like.count().when(0L).then(false).otherwise(true))
                .from(like)
                .where(like.user.idx.eq(userIdx).and(like.store.idx.eq(store.idx)));
        }
    }
}
