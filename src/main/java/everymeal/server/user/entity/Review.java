package everymeal.server.user.entity;


import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "review")
public class Review {
    @Id private String _id;
    private User user;
    private List<String> photoList;
    private String content;
    private Double grade;
    private Integer awesomeCount;
}
