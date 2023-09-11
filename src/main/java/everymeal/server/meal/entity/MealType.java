package everymeal.server.meal.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MealType {
    BREAKFAST("조식"),
    LUNCH("중식"),
    DINNER("석식"),
    SPECIAL("특식"),
    ;

    private String name;

    MealType(String name) {
        this.name = name;
    }
}
