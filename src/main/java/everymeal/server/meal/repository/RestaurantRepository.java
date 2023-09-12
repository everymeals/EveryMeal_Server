package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {}
