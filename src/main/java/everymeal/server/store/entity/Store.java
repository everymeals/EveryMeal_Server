package everymeal.server.store.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.review.entity.Image;
import everymeal.server.review.entity.Review;
import everymeal.server.university.entity.University;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String address;
    // 카카오에서 제공하는 카테고리
    private String categoryGroup;
    // 조회할때 쓰인 음식점, 카페에 대한 카테고리
    private String category;
    // 조회 기반에 따라 세분화된 카테고리
    private String categoryDetail;
    private String kakaoId;
    private String phone;
    private Integer distance;
    private String url;
    private String roadAddress;
    private String x;
    private String y;
    private Boolean isDeleted;

    @Embedded private GradeStatistics gradeStatistics;

    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_idx")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private Set<Review> reviews;

    @Builder
    public Store(
            String name,
            String address,
            String categoryGroup,
            String category,
            String categoryDetail,
            String kakaoId,
            String phone,
            Integer distance,
            String url,
            String roadAddress,
            String x,
            String y,
            GradeStatistics gradeStatistics,
            University university) {
        this.name = name;
        this.address = address;
        this.categoryGroup = categoryGroup;
        this.category = category;
        this.categoryDetail = categoryDetail;
        this.kakaoId = kakaoId;
        this.phone = phone;
        this.distance = distance;
        this.url = url;
        this.roadAddress = roadAddress;
        this.x = x;
        this.y = y;
        this.gradeStatistics = gradeStatistics;
        this.university = university;
        this.isDeleted = Boolean.FALSE;
    }

    public void addGrade(Integer grade) {
        this.gradeStatistics.addGrade(grade);
    }
}
