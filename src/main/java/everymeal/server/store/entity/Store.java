package everymeal.server.store.entity;


import everymeal.server.meal.entity.Area;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@Document(value = "store")
public class Store {
    @Id
    @Field("_id")
    private String _id;

    private String name;
    private String tag;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Double grade;
    private Integer reviewCount;
    private Integer recommendedCnt;
    private List<String> photoList;
    private Area area;
}
