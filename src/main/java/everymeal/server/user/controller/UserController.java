package everymeal.server.user.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 관련 API입니다")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ApplicationResponse<Boolean> signUp(
            @RequestBody
                    @Schema(
                            description = "유저의 기기 고유 번호",
                            defaultValue = "1234567890",
                            example = "1234567890")
                    String userDeviceId) {
        return ApplicationResponse.ok(userService.signUp(userDeviceId));
    }
}
