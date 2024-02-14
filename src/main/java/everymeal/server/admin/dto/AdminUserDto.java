package everymeal.server.admin.dto;

import static everymeal.server.global.util.aws.S3Util.getImgUrl;

import everymeal.server.admin.entity.UserDefaultProfileImage;
import io.swagger.v3.oas.annotations.media.Schema;

public class AdminUserDto {

    @Schema(description = "어드민에서 관리되는 유저의 기본 프로필 이미지 정보를 담은 응답 DTO입니다.")
    public record DefaultProfileImageRes(
            @Schema(description = "유저의 기본 프로필 이미지의 idx입니다.") Long idx,
            @Schema(description = "유저의 기본 프로필 이미지의 URL입니다.") String profileImageUrl,
            @Schema(description = "유저의 기본 프로필 이미지의 imageKey입니다.") String imageKey) {
        public static DefaultProfileImageRes of(UserDefaultProfileImage entity) {
            return new DefaultProfileImageRes(
                    entity.getIdx(),
                    getImgUrl(entity.getProfileImgUrl()),
                    entity.getProfileImgUrl());
        }
    }
}
