package everymeal.server.user.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileUpdateReq(
        @Schema(description = "닉네임", example = "연유크림") String nickName,
        @Schema(description = "프로필 이미지 key", example = "user/bc90af33-bc6a-4009-bfc8-2c3efe0b16bd")
                String profileImageKey) {}
