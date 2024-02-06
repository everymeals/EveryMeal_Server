package everymeal.server.review.repository;


import everymeal.server.review.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(
            value =
                    """
        SELECT i
        FROM images i
        LEFT JOIN reviews r ON i.review.idx = r.idx AND r.isDeleted = false
        WHERE i.isDeleted = false and r.store.idx = :storeIdx
        ORDER BY i.createdAt DESC
        LIMIT 5
    """)
    List<Image> getStoreImages(@Param(value = "storeIdx") Long storeIdx);
}
