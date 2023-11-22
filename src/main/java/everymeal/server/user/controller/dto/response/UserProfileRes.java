package everymeal.server.user.controller.dto.response;


import java.util.Map;

public record UserProfileRes(
        Long userId, String nickName, String profileImgUrl, String universityName) {
    public static UserProfileRes of(Map<String, Object> user) {
        return new UserProfileRes(
                (Long) user.get("userId"),
                (String) user.get("nickName"),
                (String) user.get("profileImgUrl"),
                (String) user.get("universityName"));
    }
}
