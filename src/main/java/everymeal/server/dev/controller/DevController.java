package everymeal.server.dev.controller;


import everymeal.server.dev.controller.dto.response.LoginRes;
import everymeal.server.dev.service.DevService;
import everymeal.server.global.dto.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/devs")
@RestController
@RequiredArgsConstructor
@Tag(name = "dev API", description = "개발 관련 API입니다")
public class DevController {

    private final DevService devService;

    @Operation(
            summary = "(개발 전용)유저 로그인",
            description =
                    """
            (개발 전용) 해당 API 운영 단계에서 빠질 예정입니다. <br>
            유저 로그인을 진행합니다. <br>
            유저 로그인 성공 시, access-token과 refresh-token을 반환합니다.
            """)
    @GetMapping("/user/login")
    public ResponseEntity<ApplicationResponse<LoginRes>> userLogin(@RequestParam String email) {
        return setRefreshToken(devService.userLogin(email));
    }

    private ResponseEntity<ApplicationResponse<LoginRes>> setRefreshToken(LoginRes response) {
        ResponseCookie cookie =
                ResponseCookie.from("refresh-token", response.refreshToken())
                        .httpOnly(true)
                        .sameSite("None")
                        .path("/")
                        .maxAge(60 * 60 * 24 * 30L)
                        //                        .secure(true)
                        .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApplicationResponse.ok(response.withoutRefreshToken()));
    }
}
