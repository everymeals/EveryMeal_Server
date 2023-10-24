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
    private String kakaoId;
    private String phone;
    private String distance;
    private String url;
    private String roadAddress;
    private String x;
    private String y;

    private Double grade;
    private Integer reviewCount;
    private Integer recommendedCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

    @Builder
    public Store(
            String name,
            String address,
            String categoryGroup,
            String category,
            String kakaoId,
            String phone,
            String distance,
            String url,
            String roadAddress,
            String x,
            String y,
            Double grade,
            Integer reviewCount,
            Integer recommendedCnt,
            University university) {
        this.name = name;
        this.address = address;
        this.categoryGroup = categoryGroup;
        this.category = category;
        this.kakaoId = kakaoId;
        this.phone = phone;
        this.distance = distance;
        this.url = url;
        this.roadAddress = roadAddress;
        this.x = x;
        this.y = y;
        this.grade = grade;
        this.reviewCount = reviewCount;
        this.recommendedCnt = recommendedCnt;
        this.university = university;
    }
}
