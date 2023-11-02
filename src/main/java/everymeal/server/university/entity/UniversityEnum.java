package everymeal.server.university.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UniversityEnum {
    MYONGJI_S("명지대학교", "명지대"),
    SUNGSIN("성신여자대학교", "성신여대"),
    SEOULWOMEN("서울여자대학교", "서울여대"),
    EMPTY("등록되지 않은 학교", "미등록");

    private String universityName;
    private String universityShortName;

    UniversityEnum(String universityName, String universityShortName) {
        this.universityName = universityName;
        this.universityShortName = universityShortName;
    }

    public static String getShortNameForUniversity(String universityName) {
        for (UniversityEnum university : UniversityEnum.values()) {
            if (university.getUniversityName().equals(universityName)) {
                return university.getUniversityShortName();
            }
        }
        return EMPTY.getUniversityShortName();
    }
}
