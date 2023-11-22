package everymeal.server.store.repository;

import static everymeal.server.store.entity.StoreSortVo.*;

import everymeal.server.store.controller.dto.response.StoreGetRes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    @PersistenceContext private final EntityManager em;

    public Page<StoreGetRes> getStores(
            Long universityIdx,
            Pageable pageable,
            String group,
            Long userIdx,
            String order,
            Integer grade) {

        // native query 생성
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(
                "SELECT s.idx, s.name, s.address, s.phone, s.category_detail, s.distance, s.grade, s.review_count, s.recommended_count, ");
        sqlBuilder.append(
                "GROUP_CONCAT(s.images, ' ') AS like_count, get_store_like_check(:userIdx) AS like_check ");
        sqlBuilder.append("FROM store s ");
        sqlBuilder.append("WHERE s.university_idx = :universityIdx ");
        sqlBuilder.append("AND s.is_deleted = false ");
        sqlBuilder.append(getWhereCaseNative(group));
        sqlBuilder.append(getGradeCaseNative(grade));
        sqlBuilder.append("ORDER BY ");
        sqlBuilder.append(getOrderCaseNative(order));
        sqlBuilder.append(" OFFSET :offset ");
        sqlBuilder.append("LIMIT :limit ");

        // native query 실행
        Query query = em.createNativeQuery(sqlBuilder.toString(), "StoreGetResMapping");
        query.setParameter("universityIdx", universityIdx);
        query.setParameter("userIdx", userIdx);
        query.setParameter("offset", pageable.getOffset());
        query.setParameter("limit", pageable.getPageSize());

        // 결과 매핑
        List<StoreGetRes> result = query.getResultList();

        // 전체 개수 조회
        String countSql =
                "SELECT COUNT(*) FROM store WHERE university_idx = :universityIdx AND is_deleted = false";
        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter("universityIdx", universityIdx);
        Long count = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(result, pageable, count);
    }

    private String getGradeCaseNative(Integer grade) {
        if (grade == null) {
            return "";
        }
        switch (grade) {
            case 1:
                return "AND s.grade BETWEEN 0 AND 1 ";
            case 2:
                return "AND s.grade BETWEEN 1 AND 2 ";
            case 3:
                return "AND s.grade BETWEEN 2 AND 3 ";
            case 4:
                return "AND s.grade BETWEEN 3 AND 4 ";
            case 5:
                return "AND s.grade BETWEEN 4 AND 5 ";
            default:
                return "AND s.grade BETWEEN 0 AND 5 ";
        }
    }

    private String getWhereCaseNative(String group) {
        switch (group) {
            case "recommend":
                return "AND s.recommended_count > 0 ";
            case "restaurant":
                return "AND (s.category_detail = '한식' OR s.category_detail = '중식' OR s.category_detail = '일식' OR s.category_detail = '양식') ";
            case "cafe":
                return "AND (s.category_detail = '카페' OR s.category_detail = '디저트') ";
            case "bar":
                return "AND s.category_detail = '술집' ";
            default:
                return "AND (s.category_detail = '기타' OR s.category_detail = '패스트푸드' OR s.category_detail = '분식') ";
        }
    }

    private String getOrderCaseNative(String order) {
        switch (order) {
            case SORT_NAME:
                return "s.name ASC";
            case SORT_DISTANCE:
                return "s.distance ASC";
            case SORT_RECOMMENDEDCNOUNT:
                return "s.recommended_count DESC";
            case SORT_REVIEWCOUNT:
                return "s.review_count DESC";
            case SORT_GRADE:
                return "s.grade DESC";
            case SORT_RECENT:
                return "s.created_at DESC";
            default:
                return "s.idx DESC";
        }
    }
}
