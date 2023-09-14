package everymeal.server.university.service;


import everymeal.server.university.controller.dto.response.UniversityListGetRes;
import java.util.List;

public interface UniversityService {

    List<UniversityListGetRes> getUniversityList(String universityName, String campusName);
}
