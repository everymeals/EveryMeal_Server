package everymeal.server.user.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
                            defaultValue = "123456789",
                            example = "1234567890")
                    String userDeviceId) {
        return ApplicationResponse.ok(userService.signUp(userDeviceId));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<UserLoginRes>> login(
            @RequestBody
                    @Schema(
                            description = "유저의 기기 고유 번호",
                            defaultValue = "123456789",
                            example = "1234567890")
                    String userDeviceId) {
        UserLoginRes response = userService.login(userDeviceId);
        ResponseCookie cookie =
                ResponseCookie.from("refresh-token", response.getRefreshToken())
                        .httpOnly(true)
                        .sameSite("None")
                        .path("/")
                        .maxAge(60 * 60 * 24 * 30L)
                        .secure(true)
                        .build();
        response.setRefreshToken(null);
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApplicationResponse.ok(response));
    }

    @Auth(require = true)
    @GetMapping("/auth")
    @Operation(summary = "유저 인증 여부")
    @SecurityRequirement(name = "bearerAuth")
    public ApplicationResponse<Boolean> isAuth(@AuthUser AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(userService.isAuth(authenticatedUser));
    }
}
