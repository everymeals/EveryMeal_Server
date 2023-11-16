package everymeal.server.store.entity;


import everymeal.server.university.entity.University;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private String address;
    private String categoryGroup;
    private String category;
    private String categoryDetail;
    private String kakaoId;
    private String phone;
    private Integer distance;
    private String url;
    private String roadAddress;
    private String x;
    private String y;

    private Double grade;
    private Integer reviewCount;
    private Integer recommendedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

}
