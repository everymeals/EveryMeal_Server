package everymeal.server.user.entity;


import everymeal.server.meal.entity.University;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String deviceId;
    private String nickName;
    private String email;

    @Enumerated(EnumType.STRING)
    private University university;

    @Builder
    public Users(String deviceId, String nickName, String email, University university) {
        this.deviceId = deviceId;
        this.nickName = nickName;
        this.email = email;
        this.university = university;
    }
}
