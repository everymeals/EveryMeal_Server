package everymeal.server.user.repository;


import everymeal.server.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {}
