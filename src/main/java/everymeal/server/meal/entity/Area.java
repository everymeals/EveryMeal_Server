package everymeal.server.meal.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(value = "area")
public class Area {
    @Id private String _id;
    private String name;
    private University university;
}
