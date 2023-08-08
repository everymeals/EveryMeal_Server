package everymeal.server.store.repository;


import everymeal.server.store.entity.Store;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MealRepository extends MongoRepository<Store, String> {}
