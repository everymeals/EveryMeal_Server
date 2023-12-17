package everymeal.server.store.repository;


import everymeal.server.store.entity.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByKakaoId(String kakaoId);

    Page<Store> findByUniversityIdxOrderByIdxDesc(Long universityIdx, Pageable pageable);
}
