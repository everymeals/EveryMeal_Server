package everymeal.server.meal.entity;

import java.awt.geom.Area;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.repository.NoRepositoryBean;

@Getter
@Builder
@Document(collation = "meal")
public class Meal {

    @Id
    private String _id;
    private String name;
    private MealInfo mealInfo;
    private String notice;
    private String info;
    private String price;
    private Area area;

}

