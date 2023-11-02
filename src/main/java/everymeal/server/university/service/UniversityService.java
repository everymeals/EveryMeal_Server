package everymeal.server.university.service;


import everymeal.server.university.controller.dto.response.UniversityListGetRes;
import java.util.List;

public interface UniversityService {

    Boolean addUniversity(String universityName, String campusName);

    List<UniversityListGetRes> getUniversities();
}
