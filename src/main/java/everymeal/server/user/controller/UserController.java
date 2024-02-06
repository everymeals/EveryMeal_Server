package everymeal.server.user.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.request.UserProfileUpdateReq;
import everymeal.server.user.controller.dto.request.WithdrawalReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.controller.dto.response.UserProfileRes;
import everymeal.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 관련 API입니다")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description =
                    """
            회원가입을 진행합니다. <br>
            회원가입 성공 시, refresh-token을 쿠키로 반환합니다.
            """)
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "회원가입 성공",
                content = @Content(schema = @Schema(implementation = UserLoginRes.class))),
        @ApiResponse(
                responseCode = "403",
                description =
                        """
                            (U0002)인증 시간이 만료되었습니다.<br>
                            (U0003)인증에 실패하였습니다.<br>
                            """,
                content = @Content(schema = @Schema())),
        @ApiResponse(
                responseCode = "409",
                description =
                        """
                            (U0004)이미 가입된 유저입니다.<br>
                            (U0005)이미 등록된 닉네임입니다.<br>
                            """,
                content = @Content(schema = @Schema())),
    })
    @PostMapping("/signup")
    public ResponseEntity<ApplicationResponse<UserLoginRes>> signUp(
            @RequestBody UserEmailSingReq request) {
        UserLoginRes response = userService.signUp(request);
        return setRefreshToken(response);
    }

    @Operation(
            summary = "로그인",
            description =
                    """
                로그인을 진행합니다. <br> 로그인 성공 시, refresh-token을 쿠키로 반환합니다.<br>
                테스트용 계정 로그인 <br>
                emailAuthToken : eyJhbGciOiJIUzUxMiJ9.eyJDTEFJTV9LRVlfRU1BSUwiOiJiZV9tYW5AbmF2ZXIuY29tIiwiQ0xBSU1fS0VZX1NFTkRfQVVUSF9QQVNTV09SRCI6IjYwMTAxNSIsImlhdCI6MTY5OTc2ODE5MH0.svqkfcczKTpcAWu8t9iMzZYI1CKd8s0p3Cj7f_eOBzRbW1Qy06IupiccGDk4Q2ABe8SEgn5ZjtZ6tsc4etRKDg <br>
                emailAuthValue : 601015""")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = @Content(schema = @Schema(implementation = UserLoginRes.class))),
        @ApiResponse(
                responseCode = "404",
                description = "(U0001)유저를 찾을 수 없습니다.",
                content = @Content(schema = @Schema()))
    })
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<UserLoginRes>> login(
            @RequestBody UserEmailLoginReq request) {
        UserLoginRes response = userService.login(request);
        return setRefreshToken(response);
    }

    @PostMapping("/email")
    @Operation(
            summary = "이메일 전송",
            description =
                    "이메일 인증을 진행합니다. <br> Response에 5분 동안 유효한 JWT 토큰이 담기는데 해당 토큰에는 발송 값이 들어있습니다.")
    public ApplicationResponse<UserEmailAuthRes> emailAuth(@RequestBody UserEmailAuthReq request) {
        return ApplicationResponse.ok(userService.emailAuth(request));
    }

    @GetMapping("/email/verify")
    @Operation(
            summary = "이메일 인증 확인",
            description = "이메일 인증을 확인합니다. <br> Request에는 이메일 인증 시 발송된 값이 담겨야 합니다.")
    public ApplicationResponse<Boolean> verifyEmailAuth(
            @Schema(
                            description = "이메일 인증 API에서 반환된 토큰 값",
                            defaultValue = "eyBdvsjfgsdkgb",
                            example = "eyBdvsjfgsdkgb")
                    @NotBlank(message = "이메일 인증 토큰을 입력해주세요.")
                    @RequestParam
                    String emailAuthToken,
            @Schema(description = "이메일 유저가 입력한 인증 값", defaultValue = "123456", example = "123456")
                    @NotBlank(message = "인증번호를 입력해주세요.")
                    String emailAuthValue) {
        return ApplicationResponse.ok(userService.verifyEmailAuth(emailAuthToken, emailAuthValue));
    }

    @GetMapping("/email")
    @Operation(
            summary = "이미 가입된 유저인지 확인",
            description = "이미 가입된 유저인지 확인합니다. <br> 가입된 유저 true, 가입되지 않은 유저 false")
    public ApplicationResponse<Boolean> checkUser(
            @Schema(description = "이메일", example = "test@gmail.com") @RequestParam String email) {
        return ApplicationResponse.ok(userService.checkUser(email));
    }

    @Auth(require = true)
    @GetMapping("/profile")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "인증된 사용자의 프로필 정보 조회", description = "인증된 사용자의 프로필 정보를 조회합니다.")
    public ApplicationResponse<UserProfileRes> getUserProfile(
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(userService.getUserProfile(authenticatedUser));
    }

    @Auth(require = true)
    @PutMapping("/profile")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "인증된 사용자의 프로필 정보 수정", description = "인증된 사용자의 프로필 정보를 수정합니다.")
    @ApiResponse(
            responseCode = "409",
            description = """
                    (U0005)이미 등록된 닉네임입니다.<br>
                    """,
            content = @Content(schema = @Schema()))
    public ApplicationResponse<Boolean> updateUserProfile(
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser,
            @RequestBody UserProfileUpdateReq userProfileUpdateReq) {
        return ApplicationResponse.ok(
                userService.updateUserProfile(authenticatedUser, userProfileUpdateReq));
    }

    @Auth(require = true)
    @PostMapping("/withdrawal")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "회원탈퇴", description = "서비스 회원 탈퇴를 합니다.")
    @ApiResponse(
            responseCode = "404",
            description = """
                    (U0001)등록된 유저가 아닙니다.<br>
                    """,
            content = @Content(schema = @Schema()))
    public ApplicationResponse<Boolean> withdrawal(
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser,
            @RequestBody WithdrawalReq withdrawalReq) {
        return ApplicationResponse.ok(userService.withdrawal(authenticatedUser, withdrawalReq));
    }

    @GetMapping("/token/access")
    @Operation(
            summary = "Access Token 재발급",
            description = "Refresh Token을 이용하여 Access Token을 재발급합니다.")
    public ApplicationResponse<String> reissueAccessToken(
            @CookieValue(name = "refresh-token") String refreshToken) {
        return ApplicationResponse.ok(userService.reissueAccessToken(refreshToken));
    }

    private ResponseEntity<ApplicationResponse<UserLoginRes>> setRefreshToken(
            UserLoginRes response) {
        ResponseCookie cookie =
                ResponseCookie.from("refresh-token", response.refreshToken())
                        .httpOnly(true)
                        .sameSite("None")
                        .path("/")
                        .maxAge(60 * 60 * 24 * 30L)
                        .secure(true)
                        .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApplicationResponse.ok(response.withoutRefreshToken()));
    }
}
