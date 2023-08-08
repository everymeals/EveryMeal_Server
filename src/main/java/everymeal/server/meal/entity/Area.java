package everymeal.server.meal.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "area")
public class Area {
    @Id private String _id;
    private String name;
    private University university;
}
