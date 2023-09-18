package everymeal.server.global.util.authresolver.entity;


import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticatedUser {
    private Long idx;
    private String email;
    private String nickName;

    @Builder
    public AuthenticatedUser(Long idx, String email, String nickName) {
        this.idx = idx;
        this.email = email;
        this.nickName = nickName;
    }
}
