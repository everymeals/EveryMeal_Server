package everymeal.server.user.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collation = "user")
public class User {

    @Id private String deviceId;
    private String nickName;
    private String email;

    @Builder
    public User(String deviceId, String nickName, String email) {
        this.deviceId = deviceId;
        this.nickName = nickName;
        this.email = email;
    }
}
