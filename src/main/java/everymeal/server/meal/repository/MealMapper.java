package everymeal.server.meal.repository;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MealMapper {
    List<Map<String, Object>> findDayList(
            @Param("offeredAt") String offeredAt,
            @Param("universityName") String universityName,
            @Param("campusName") String campusName);

    List<Map<String, Object>> findWeekList(
            @Param("weeklyDates") List<String> weeklyDates,
            @Param("universityName") String universityName,
            @Param("campusName") String campusName);
}
