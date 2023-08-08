package everymeal.server.user.entity;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collation = "user")
public class User {

    @Id private String deviceId;
    private String nickName;
    private String email;
}
