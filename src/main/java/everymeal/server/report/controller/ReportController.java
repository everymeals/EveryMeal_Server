package everymeal.server.report.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.report.dto.ReportDto;
import everymeal.server.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/review/{reviewIdx}")
    @Operation(
            summary = "리뷰 신고",
            description =
                    """
              리뷰 신고를 진행합니다. <br>
              로그인이 필요한 기능입니다.
              """)
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "리뷰 신고 성공",
                content = @Content(schema = @Schema())),
    })
    @Auth(require = true)
    @SecurityRequirement(name = "jwt-user-auth")
    public ApplicationResponse<Boolean> reportReview(
            @Schema(description = "리뷰 아이디") @PathVariable Long reviewIdx,
            @Schema(description = "리뷰 신고 사유") @RequestBody @Valid ReportDto.ReportReviewReq request,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser user) {
        return ApplicationResponse.ok(
                reportService.reportReview(reviewIdx, request, user.getIdx()));
    }
}
