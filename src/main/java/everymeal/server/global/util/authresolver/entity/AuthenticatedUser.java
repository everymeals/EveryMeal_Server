package everymeal.server.global.util.authresolver.entity;


import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticatedUser {
    private Long idx;
    private String deviceId;
    private String nickName;

    @Builder
    public AuthenticatedUser(Long idx, String deviceId, String nickName) {
        this.idx = idx;
        this.deviceId = deviceId;
        this.nickName = nickName;
    }
}
