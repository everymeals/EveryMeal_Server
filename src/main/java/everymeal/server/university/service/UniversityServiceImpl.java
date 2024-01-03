package everymeal.server.university.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
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
    @Transactional
    public Boolean addUniversity(String universityName, String campusName) {
        University university =
                University.builder().name(universityName).campusName(campusName).build();
        universityRepository.save(university);
        return true;
    }

    @Override
    public List<UniversityListGetRes> getUniversities() {
        List<University> universities = universityRepository.findAllByIsDeletedFalse();
        return UniversityListGetRes.of(universities);
    }

    public University getUniversity(String universityName, String campusName) {
        return universityRepository
                .findByNameAndCampusNameAndIsDeletedFalse(universityName, campusName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ExceptionList.UNIVERSITY_NOT_FOUND));
    }
}
