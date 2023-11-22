package everymeal.server.store.repository;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreMapper {

    List<Map<String, Object>> getStores(
            @Param("universityIdx") Long universityIdx,
            @Param("limit") Integer limit,
            @Param("offset") Long offset,
            @Param("group") String group,
            @Param("userIdx") Long userIdx,
            @Param("order") String order,
            @Param("grade") Integer grade);

    Long getStoreCount(
            @Param("universityIdx") Long universityIdx,
            @Param("limit") Integer limit,
            @Param("offset") Long offset,
            @Param("group") String group,
            @Param("userIdx") Long userIdx,
            @Param("order") String order,
            @Param("grade") Integer grade);
}
