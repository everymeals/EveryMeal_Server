package everymeal.server.user.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.university.entity.University;
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

    private String deviceId;

    private String nickName;

    private String email;

    private Boolean isDeleted;

    @ManyToOne private University university;

    @Builder
    public User(String deviceId, String nickName, String email, University university) {
        this.deviceId = deviceId;
        this.nickName = nickName;
        this.email = email;
        this.university = university;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
