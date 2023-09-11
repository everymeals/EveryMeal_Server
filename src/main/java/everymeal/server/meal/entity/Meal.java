package everymeal.server.meal.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "meal")
public class Meal {

    @Id private String _id;
    private String menu;
    private MealType mealType;
    private MealStatus mealStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate offeredAt;

    private Double price;
    private Restaurant restaurant;
}
