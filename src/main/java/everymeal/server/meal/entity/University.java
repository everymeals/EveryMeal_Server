package everymeal.server.meal.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum University {
    MYONGJI_Y("명지대학교 용인"),
    MYONGJI_S("명지대학교 서울"),
    SUNGSIN("성신여자대학교"),
    SEOULWOMEN("서울여자대학교");

    private String name;

    University(String name) {
        this.name = name;
    }
}
