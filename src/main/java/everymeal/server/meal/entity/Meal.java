package everymeal.server.meal.entity;


import java.awt.geom.Area;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collation = "meal")
public class Meal {

    @Id private String _id;
    private String name;
    private MealInfo mealInfo;
    private String notice;
    private String info;
    private String price;
    private Area area;
}
