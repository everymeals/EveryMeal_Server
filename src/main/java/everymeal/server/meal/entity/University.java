package everymeal.server.meal.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum University {
    MYONGJI_S("명지대학교 서울캠퍼스"),
    MYONGJI_Y("명지대학교 용인캠퍼스"),
    SUNGSIN_S("성신여자대학교 수정캠퍼스"),
    SUNGSIN_Y("성신여자대학교 운정캠퍼스"),
    SEOULWOMEN("서울여자대학교");

    private String name;

    University(String name) {
        this.name = name;
    }
}
