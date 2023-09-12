package everymeal.server.user.entity;


import everymeal.server.meal.entity.University;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class User {

    @Id
    private String deviceId;
    private String nickName;
    private String email;
    private University university;

    @Builder
    public User(String deviceId, String nickName, String email, University university) {
        this.deviceId = deviceId;
        this.nickName = nickName;
        this.email = email;
        this.university = university;
    }
}
