package everymeal.server.store.entity;


import everymeal.server.meal.entity.Restaurant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String tag;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Double grade;
    private Integer reviewCount;
    private Integer recommendedCnt;
    private String photoList;

}
