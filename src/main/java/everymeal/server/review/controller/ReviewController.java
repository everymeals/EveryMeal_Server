package everymeal.server.review.controller;

import static everymeal.server.review.entity.ReviewQueryParamVo.FILTER_ALL;
import static everymeal.server.review.entity.ReviewQueryParamVo.FILTER_TODAY;
import static everymeal.server.review.entity.ReviewQueryParamVo.SORT_LIKE;
import static everymeal.server.review.entity.ReviewQueryParamVo.SORT_RECENT;

import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.review.dto.request.ReviewCreateReq;
import everymeal.server.review.dto.response.ReviewDto.ReviewGetRes;
import everymeal.server.review.dto.response.ReviewDto.ReviewQueryParam;
import everymeal.server.review.dto.response.ReviewDto.ReviewTodayGetRes;
import everymeal.server.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        @ApiResponse(responseCode = "200", description = "학식 리뷰 등록 성공"),
    })
    @Auth(require = true)
    @PostMapping
    @SecurityRequirement(name = "jwt-user-auth")
    public ApplicationResponse<Long> createReview(
            @RequestBody ReviewCreateReq request,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser user) {
        return ApplicationResponse.create(reviewService.createReview(request, user.getIdx()));
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
    @Auth(require = true)
    @PutMapping("/{reviewIdx}")
    @SecurityRequirement(name = "jwt-user-auth")
    public ApplicationResponse<Long> updateReview(
            @Schema(description = "리뷰 PK", defaultValue = "1") @PathVariable Long reviewIdx,
            @RequestBody ReviewCreateReq request,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser user) {
        return ApplicationResponse.ok(
                reviewService.updateReview(request, user.getIdx(), reviewIdx));
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
    @Auth(require = true)
    @SecurityRequirement(name = "jwt-user-auth")
    @DeleteMapping("/{reviewIdx}")
    public ApplicationResponse<Boolean> deleteReview(
            @Schema(description = "리뷰 PK", defaultValue = "1") @PathVariable Long reviewIdx,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser user) {
        return ApplicationResponse.ok(reviewService.deleteReview(user.getIdx(), reviewIdx));
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
            @Schema(description = "조회하고자 하는 데이터의 시작점 idx", example = "1")
                    @RequestParam(required = true)
                    Long cursorIdx,
            @Schema(description = "식당 idx", example = "1") @RequestParam(required = true)
                    Long restaurantIdx,
            @Schema(description = "한 페이지에서 보고자 하는 데이터의 개수", example = "8")
                    @RequestParam(required = true)
                    int pageSize,
            @RequestParam(value = "order", required = false, defaultValue = "recent")
                    @Schema(
                            title = "정렬 기준",
                            description = "정렬 기준은 기획에 따라 변경 가능합니다.",
                            allowableValues = {SORT_RECENT, SORT_LIKE})
                    String order,
            @RequestParam(value = "filter", required = false, defaultValue = "all")
                    @Schema(
                            title = "오늘 먹은 것만 필터",
                            description = "오늘 먹은 것만 필터링합니다.",
                            allowableValues = {FILTER_ALL, FILTER_TODAY})
                    String filter) {
        ReviewQueryParam queryParam =
                new ReviewQueryParam(cursorIdx, restaurantIdx, pageSize, order, filter);
        return ApplicationResponse.ok(reviewService.getReviewWithNoOffSetPaging(queryParam));
    }

    @PostMapping("/mark")
    @Operation(
            summary = "리뷰 좋아요/싫어요",
            description =
                    """
  리뷰 좋아요/싫어요를 진행합니다. <br>
  true -> 리뷰에 대한 좋아요 /싫어요 성공
  false -> 이미 리뷰에 대한 좋아요/싫어요가 있는 경우
  """)
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "리뷰 좋아요/싫어요 성공",
                content = @Content(schema = @Schema())),
    })
    @Auth(require = true)
    @SecurityRequirement(name = "jwt-user-auth")
    public ApplicationResponse<Boolean> markReview(
            @Schema(description = "리뷰 idx", example = "1") @RequestParam Long reviewIdx,
            @Schema(description = "좋아요/싫어요 여부", example = "true") @RequestParam boolean isLike,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser user) {
        return ApplicationResponse.ok(reviewService.markReview(reviewIdx, isLike, user.getIdx()));
    }

    @Operation(
            summary = "오늘 먹었어요. 리뷰 조회",
            description = """
  '오늘 먹었어요.' 학식 리뷰 조회를 진행합니다. <br>
  """)
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "리뷰 조회 성공",
                content = @Content(schema = @Schema(implementation = ReviewGetRes.class))),
    })
    @GetMapping("/today")
    public ApplicationResponse<ReviewTodayGetRes> getTodayReview(
            @RequestParam @Schema(description = "식당 아이디", defaultValue = "1") Long restaurantIdx,
            @RequestParam
                    @Schema(description = "조회하고자 하는 날짜 ( yyyy-MM-dd )", defaultValue = "2023-10-01")
                    String offeredAt) {
        return ApplicationResponse.ok(reviewService.getTodayReview(restaurantIdx, offeredAt));
    }

    @Auth(require = true)
    @Operation(summary = "주변 식당 리뷰 생성", description = """
    주변 식당 리뷰 생성합니다. <br>
    """)
    @SecurityRequirement(name = "jwt-user-auth")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "리뷰 생성 성공",
                content = @Content(schema = @Schema(implementation = Long.class))),
    })
    @PostMapping("/store")
    public ApplicationResponse<Long> createReviewByStore(
            @RequestBody ReviewCreateReq request,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser user) {
        return ApplicationResponse.ok(reviewService.createReviewByStore(request, user.getIdx()));
    }
}
