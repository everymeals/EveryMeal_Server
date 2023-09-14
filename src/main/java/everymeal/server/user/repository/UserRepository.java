package everymeal.server.user.repository;


import everymeal.server.user.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByDeviceId(String deviceId);
}
