package everymeal.server.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import everymeal.server.global.IntegrationTestSupport;
import everymeal.server.university.controller.dto.response.UniversityListGetRes;
import everymeal.server.university.entity.University;
import everymeal.server.university.repository.UniversityRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UniversityServiceImplTest extends IntegrationTestSupport {

    @Autowired private UniversityService universityService;
    @Autowired private UniversityRepository universityRepository;

    @AfterEach
    void tearDown() {
        universityRepository.deleteAllInBatch();
    }

    @DisplayName("대학을 추가한다.")
    @Test
    void addUniversity() {
        // given
        String universityName = "명지대학교";
        String campusName = "인문캠퍼스";

        // when
        Boolean response = universityService.addUniversity(universityName, campusName);

        // then
        List<University> universities =
                universityRepository.findByNameAndCampusNameAndIsDeletedFalse(
                        universityName, campusName);
        assertEquals(response, true);
        assertEquals(universities.get(0).getName(), universityName);
    }

    @DisplayName("대학 리스트를 조회한다.")
    @Test
    void getUniversities() {
        // given
        String universityName = "명지대학교";
        String campusName = "인문캠퍼스";

        // when
        University save = universityRepository.save(getUniversity(universityName, campusName));

        List<UniversityListGetRes> universityList = universityService.getUniversities();

        // then

        assertEquals(universityList.get(0).universityName(), universityName);
    }

    private University getUniversity(String universityName, String campusName) {
        return University.builder().name(universityName).campusName(campusName).build();
    }
}
