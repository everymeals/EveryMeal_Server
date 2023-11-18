package everymeal.server.user.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.review.entity.Review;
import everymeal.server.review.entity.ReviewMark;
import everymeal.server.university.entity.University;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(
        indexes = {
            @Index(name = "idx__email", columnList = "email"),
            @Index(name = "idx__nickname", columnList = "nickname")
        })
@Entity(name = "users")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(unique = true, length = 50)
    private String nickname;

    @Column(unique = true)
    private String email;

    private Boolean isDeleted;

    private String profileImgUrl;

    @ManyToOne private University university;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<ReviewMark> reviewMarks = new HashSet<>();

    @Builder
    public User(String nickname, String email, String profileImgUrl, University university) {
        this.nickname = nickname;
        this.email = email;
        this.isDeleted = Boolean.FALSE;
        this.profileImgUrl = profileImgUrl;
        this.university = university;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
