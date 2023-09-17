package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Restaurant;
import everymeal.server.university.entity.University;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByUniversityAndUseYnTrue(University university);

    Optional<Restaurant> findByName(String name);
}
