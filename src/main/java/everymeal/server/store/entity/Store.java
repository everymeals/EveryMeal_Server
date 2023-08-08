package everymeal.server.store.entity;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "store")
public class Store {
    @Id private String _id;
    private String name;
    private String tag;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Double grade;
    private Integer reviewCount;
    private Integer recommendedCnt;
    private List<String> photoList;
}
