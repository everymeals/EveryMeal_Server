package everymeal.server.admin.repository;


import everymeal.server.admin.entity.UserDefaultProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDefaultProfileImageRepository
        extends JpaRepository<UserDefaultProfileImage, Long> {}
