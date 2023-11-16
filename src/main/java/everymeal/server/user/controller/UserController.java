package everymeal.server.user.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailLoginReq;
import everymeal.server.user.controller.dto.request.UserEmailSingReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
            description = """
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
    public ApplicationResponse<Boolean> verifyEmailAuth(@RequestBody UserEmailLoginReq request) {
        return ApplicationResponse.ok(userService.verifyEmailAuth(request));
    }

    @GetMapping("/email")
    @Operation(
            summary = "이미 가입된 유저인지 확인",
            description = "이미 가입된 유저인지 확인합니다. <br> 가입된 유저 true, 가입되지 않은 유저 false")
    public ApplicationResponse<Boolean> checkUser(
            @Schema(description = "이메일", example = "test@gmail.com") @RequestParam String email) {
        return ApplicationResponse.ok(userService.checkUser(email));
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
