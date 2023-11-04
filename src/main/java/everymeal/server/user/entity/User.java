package everymeal.server.user.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.university.entity.University;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity(name = "users")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(unique = true)
    private String nickName;

    @Column(unique = true)
    private String email;

    private Boolean isDeleted;

    private String profileImgUrl;

    @ManyToOne private University university;

    @Builder
    public User(
            String nickName,
            String email,
            Boolean isDeleted,
            String profileImgUrl,
            University university) {
        this.nickName = nickName;
        this.email = email;
        this.isDeleted = Boolean.TRUE;
        this.profileImgUrl = profileImgUrl;
        this.university = university;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
