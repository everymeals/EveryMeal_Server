package everymeal.server.user.repository;


import everymeal.server.store.entity.Store;
import everymeal.server.user.entity.Like;
import everymeal.server.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdxAndStoreIdx(Long userIdx, Long storeIdx);

    Optional<Like> findByUserAndStore(User user, Store store);
}
