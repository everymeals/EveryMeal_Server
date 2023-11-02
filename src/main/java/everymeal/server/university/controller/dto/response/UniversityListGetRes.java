package everymeal.server.university.controller.dto.response;


import everymeal.server.university.entity.University;
import everymeal.server.university.entity.UniversityEnum;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record UniversityListGetRes(Long idx,
    String universityName,
    String campusName,
    String universityShortName) {

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
