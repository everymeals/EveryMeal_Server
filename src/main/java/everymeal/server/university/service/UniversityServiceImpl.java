package everymeal.server.university.service;


import everymeal.server.university.controller.dto.response.UniversityListGetRes;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    @Override
    public List<UniversityListGetRes> getUniversityList(String universityName, String campusName) {
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse(
                        universityName, campusName);
        return UniversityListGetRes.of(universities);
    }
}
