package everymeal.server.meal.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(indexes = {@Index(name = "idx__mealType__offeredAt", columnList = "mealType, offeredAt")})
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String menu;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private MealStatus mealStatus;

    private LocalDate offeredAt;

    private Double price;

    @Enumerated(EnumType.STRING)
    private MealCategory category;

    @ManyToOne private Restaurant restaurant;

    @Builder
    public Meal(
            String menu,
            MealType mealType,
            MealStatus mealStatus,
            LocalDate offeredAt,
            Double price,
            MealCategory category,
            Restaurant restaurant) {
        this.menu = menu;
        this.mealType = mealType;
        this.mealStatus = mealStatus;
        this.offeredAt = offeredAt;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
    }
}
