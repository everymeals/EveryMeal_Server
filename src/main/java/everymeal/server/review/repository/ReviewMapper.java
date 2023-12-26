package everymeal.server.review.repository;


import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReviewMapper {
    Map<String, Object> findTodayReview(
            @Param("restaurantIdx") Long restaurantIdx, @Param("offeredAt") String offeredAt);
}
