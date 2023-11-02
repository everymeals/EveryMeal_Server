package everymeal.server.university.controller.dto.response;


import everymeal.server.university.entity.University;
import everymeal.server.university.entity.UniversityEnum;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UniversityListGetRes {

    private Long idx;
    private String universityName;
    private String campusName;
    private String universityShortName;

    public static List<UniversityListGetRes> of(List<University> universities) {
        return universities.stream()
                .map(
                        university ->
                                UniversityListGetRes.builder()
                                        .idx(university.getIdx())
                                        .universityName(university.getName())
                                        .campusName(university.getCampusName())
                                        .universityShortName(
                                                UniversityEnum.getShortNameForUniversity(
                                                        university.getName()))
                                        .build())
                .toList();
    }
}
