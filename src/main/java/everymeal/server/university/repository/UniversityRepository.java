package everymeal.server.university.repository;


import everymeal.server.university.entity.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findByNameAndCampusNameAndIsDeletedFalse(
            String universityName, String campusName);

    List<University> findAllByIsDeletedFalse();
}
