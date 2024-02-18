package everymeal.server.university.service;


import everymeal.server.university.controller.dto.response.UniversityListGetRes;
import everymeal.server.university.entity.University;
import java.util.List;

public interface UniversityService {

    Boolean addUniversity(String universityName, String campusName);

    List<UniversityListGetRes> getUniversities();

    University getUniversity(Long universityIdx);
}
