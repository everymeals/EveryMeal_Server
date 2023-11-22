package everymeal.server.user.repository;


import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    Map<String, Object> getUserProfile(@Param("idx") Long idx);
}
