package everymeal.server.university.controller.dto.response;


import everymeal.server.university.entity.University;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UniversityListGetRes {

    private String universityName;
    private String campusName;

    public static List<UniversityListGetRes> of(List<University> universities) {
        return universities.stream()
                .map(
                        university ->
                                UniversityListGetRes.builder()
                                        .universityName(university.getName())
                                        .campusName(university.getCampusName())
                                        .build())
                .toList();
    }
}
