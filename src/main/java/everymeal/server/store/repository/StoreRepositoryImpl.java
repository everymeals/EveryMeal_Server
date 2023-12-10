package everymeal.server.store.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory query;

    private EntityManager entityManager;
}
