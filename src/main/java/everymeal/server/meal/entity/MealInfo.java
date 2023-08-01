package everymeal.server.meal.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(value = "meal-info")
public class MealInfo {
    @Field(name = "meals")
    private String meals;
    private String type;
}
