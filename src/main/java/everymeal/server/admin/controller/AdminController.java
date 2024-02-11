package everymeal.server.admin.controller;


import everymeal.server.admin.dto.AdminUserDto.DefaultProfileImageRes;
import everymeal.server.admin.service.AdminUserService;
import everymeal.server.global.dto.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "어드민에서 관리되는 데이터 관련 API입니다.")
public class AdminController {

    private final AdminUserService adminUserService;

    @Operation(
            summary = "유저의 기본 프로필 이미지 정보를 반환합니다.",
            description =
                    "유저의 기본 프로필 이미지 정보를 반환합니다. <br/>"
                            + "피그마에 노출되는 순서대로 반환합니다. <br/>"
                            + "기본 이미지를 선택 시, imgUrl에 imageKey를 넣어주세요. <br/>")
    @GetMapping("/default-profile-images")
    public ApplicationResponse<List<DefaultProfileImageRes>> getDefaultProfileImages() {
        return ApplicationResponse.ok(adminUserService.getDefaultProfileImages());
    }
}
