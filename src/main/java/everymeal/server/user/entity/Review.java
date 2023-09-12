package everymeal.server.user.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Table
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    private Long index;

    private List<String> photoList;
    private String content;
    private Double grade;
    private Integer awesomeCount;

    private User user;
    private Long id;


}
