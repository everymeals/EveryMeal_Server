package everymeal.server.meal.entity;


import everymeal.server.meal.controller.dto.request.MealRegisterReq;
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

    private Double price = 0.0;

    @Enumerated(EnumType.STRING)
    private MealCategory category;

    @ManyToOne private Restaurant restaurant;

    @Builder
    public Meal(MealRegisterReq mealRegisterReq, Restaurant restaurant) {
        this.menu = mealRegisterReq.menu();
        this.mealType = MealType.valueOf(mealRegisterReq.mealType());
        this.mealStatus = MealStatus.valueOf(mealRegisterReq.mealStatus());
        this.offeredAt = mealRegisterReq.offeredAt();
        if (mealRegisterReq.price() != null) this.price = mealRegisterReq.price();
        this.category = MealCategory.valueOf(mealRegisterReq.category());
        this.restaurant = restaurant;
    }
}
