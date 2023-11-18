package everymeal.server.review.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewGetRes;
import everymeal.server.review.service.ReviewService;
import everymeal.server.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reviews")
@RestController
@RequiredArgsConstructor
@Tag(name = "Review API", description = "리뷰 관련 API 입니다.")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(
            summary = "학식 리뷰 작성",
            description = """
  학식 리뷰 작성을 진행합니다. <br>
  로그인이 필요한 기능입니다.
  """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리뷰 등록 성공"),
    })
    @Auth
    @PostMapping
    public ApplicationResponse<Boolean> createReview(
            @RequestBody ReviewCreateReq request, @Schema(hidden = true) @AuthUser User user) {
        return ApplicationResponse.create(reviewService.createReview(request, user));
    }

    @Operation(
            summary = "학식 리뷰 수정",
            description = """
  학식 리뷰 수정을 진행합니다. <br>
  로그인이 필요한 기능입니다.
  """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
        @ApiResponse(
                responseCode = "404",
                description = """
                  (R0001)등록된 리뷰가 아닙니다.<br>
                  """),
    })
    @Auth
    @PutMapping("/{reviewIdx}")
    public ApplicationResponse<Boolean> updateReview(
            @Schema(description = "리뷰 PK", defaultValue = "1") @PathVariable Long reviewIdx,
            @RequestBody ReviewCreateReq request,
            @Schema(hidden = true) @AuthUser User user) {
        return ApplicationResponse.ok(reviewService.updateReview(request, user, reviewIdx));
    }

    @Operation(
            summary = "학식 리뷰 삭제",
            description = """
  학식 리뷰 삭제를 진행합니다. <br>
  로그인이 필요한 기능입니다.
  """)
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "리뷰 삭제 성공",
                content = @Content(schema = @Schema())),
        @ApiResponse(
                responseCode = "404",
                description = """
                  (R0001)등록된 리뷰가 아닙니다.<br>
                  """,
                content = @Content(schema = @Schema())),
    })
    @Auth
    @DeleteMapping("/{reviewIdx}")
    public ApplicationResponse<Boolean> deleteReview(
            @Schema(description = "리뷰 PK", defaultValue = "1") @PathVariable Long reviewIdx,
            @Schema(hidden = true) @AuthUser User user) {
        return ApplicationResponse.ok(reviewService.deleteReview(user, reviewIdx));
    }

    @Operation(summary = "학식 리뷰 페이징 조회", description = """
  학식 리뷰 조회를 진행합니다. <br>
  """)
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "리뷰 조회 성공",
                content = @Content(schema = @Schema(implementation = ReviewGetRes.class))),
    })
    @GetMapping
    public ApplicationResponse<ReviewGetRes> getReviewWithNoOffSetPaging(
            @Schema(description = "조회하고자 하는 데이터의 시작점 idx", example = "1") @RequestParam
                    Long cursorIdx,
            @Schema(description = "학식 idx", example = "1") @RequestParam Long mealIdx,
            @Schema(description = "한 페이지에서 보고자 하는 데이터의 개수", example = "8") @RequestParam
                    int pageSize) {
        return ApplicationResponse.ok(
                reviewService.getReviewWithNoOffSetPaging(cursorIdx, mealIdx, pageSize));
    }
}
