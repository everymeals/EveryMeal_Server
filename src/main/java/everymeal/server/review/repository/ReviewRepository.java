package everymeal.server.review.repository;


import everymeal.server.review.entity.Review;
import everymeal.server.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByIdxAndUser(Long id, User user);
}
