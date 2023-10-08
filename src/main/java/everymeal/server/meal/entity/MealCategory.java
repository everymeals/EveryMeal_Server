package everymeal.server.meal.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum MealCategory {
    DEFAULT("단일메뉴"),
    KOREAN("한식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식"),
    SNACKBAR("분식");
    private String value;

    MealCategory(String value) {
        this.value = value;
    }
}
