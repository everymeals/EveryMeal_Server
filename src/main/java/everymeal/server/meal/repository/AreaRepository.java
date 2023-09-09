package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Area;
import everymeal.server.meal.entity.University;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends MongoRepository<Area, String> {
    Optional<Area> findByUniversity(University university);
}
