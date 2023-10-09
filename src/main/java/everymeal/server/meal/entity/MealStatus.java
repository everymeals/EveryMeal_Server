package everymeal.server.meal.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum MealStatus {
    OPEN("운영"),
    CLOSED("미운영"),
    SHORT_OPEN("단축운영"),
    ;

    private String value;

    MealStatus(String value) {
        this.value = value;
    }
}
