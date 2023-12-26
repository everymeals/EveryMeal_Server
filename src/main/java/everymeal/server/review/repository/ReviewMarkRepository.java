package everymeal.server.review.repository;


import everymeal.server.review.entity.ReviewMark;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewMarkRepository extends JpaRepository<ReviewMark, Long> {

    Optional<ReviewMark> findByReviewIdxAndUserIdx(Long reviewIdx, Long userIdx);
}
