package everymeal.server.store.controller;

import static everymeal.server.store.entity.StoreSortVo.SORT_DISTANCE;
import static everymeal.server.store.entity.StoreSortVo.SORT_GRADE;
import static everymeal.server.store.entity.StoreSortVo.SORT_NAME;
import static everymeal.server.store.entity.StoreSortVo.SORT_RECENT;
import static everymeal.server.store.entity.StoreSortVo.SORT_RECOMMENDEDCNOUNT;
import static everymeal.server.store.entity.StoreSortVo.SORT_REVIEWCOUNT;
import static everymeal.server.store.entity.StoreSortVo.SORT_REVIEWMARKCOUNT;

import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.store.controller.dto.response.LikedStoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.controller.dto.response.StoreGetReviewRes;
import everymeal.server.store.controller.dto.response.StoresGetReviews;
import everymeal.server.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/stores")
@RestController
@RequiredArgsConstructor
@Tag(name = "Store API", description = "학교 주변 식당 관련 API입니다")
public class StoreController {

    private final StoreService storeService;

    @Auth(require = false)
    @GetMapping("/campus/{campusIdx}")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "학교 주변 식당 조회", description = "학교 주변 식당을 조회합니다")
    public ApplicationResponse<Page<StoreGetRes>> getStores(
            @PathVariable(value = "campusIdx")
                    @Schema(title = "캠퍼스 키 값", description = "캠퍼스 키 값", example = "1")
                    Long campusIdx,
            @RequestParam(value = "offset", defaultValue = "0")
                    @Schema(title = "페이지 번호", example = "0", description = "페이지 번호는 0부터 시작합니다.")
                    Integer offset,
            @RequestParam(value = "limit", defaultValue = "10")
                    @Schema(
                            title = "Data 갯수",
                            example = "10",
                            description = "한 페이지에 보여지는 데이터 수 입니다.")
                    Integer limit,
            @RequestParam(value = "order")
                    @Schema(
                            title = "정렬 기준",
                            description = "정렬 기준은 기획에 따라 변경 가능합니다.",
                            allowableValues = {
                                SORT_NAME,
                                SORT_DISTANCE,
                                SORT_RECOMMENDEDCNOUNT,
                                SORT_REVIEWCOUNT,
                                SORT_GRADE,
                                SORT_RECENT
                            })
                    String order,
            @RequestParam(value = "group", required = false, defaultValue = "all")
                    @Schema(
                            title = "그룹",
                            description = "그룹",
                            allowableValues = {
                                "all",
                                "etc",
                                "recommend",
                                "restaurant",
                                "cafe",
                                "bar",
                                "korean",
                                "chinese",
                                "japanese",
                                "western",
                            })
                    String group,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser,
            @RequestParam(value = "grade", required = false)
                    @Schema(
                            title = "평점",
                            description = "평점",
                            allowableValues = {"1", "2", "3", "4", "5"})
                    Integer grade) {

        return ApplicationResponse.ok(
                storeService.getStores(
                        campusIdx,
                        PageRequest.of(offset, limit),
                        group,
                        authenticatedUser == null ? null : authenticatedUser.getIdx(),
                        order,
                        grade));
    }

    @Auth(require = true)
    @GetMapping("/likes")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "인증된 사용자의 저장 목록 조회", description = "인증된 사용자의 저장 목록을 조회합니다.")
    public ApplicationResponse<Page<LikedStoreGetRes>> getUserLikesStore(
            @RequestParam(value = "campusIdx")
                    @Schema(title = "캠퍼스 키 값", description = "캠퍼스 키 값", example = "1")
                    Long campusIdx,
            @RequestParam(value = "offset", defaultValue = "0")
                    @Schema(title = "페이지 번호", example = "0", description = "페이지 번호는 0부터 시작합니다.")
                    Integer offset,
            @RequestParam(value = "limit", defaultValue = "10")
                    @Schema(
                            title = "Data 갯수",
                            example = "10",
                            description = "한 페이지에 보여지는 데이터 수 입니다.")
                    Integer limit,
            @RequestParam(value = "group", required = false, defaultValue = "all")
                    @Schema(
                            title = "그룹",
                            description = "그룹",
                            allowableValues = {
                                "all",
                                "etc",
                                "recommend",
                                "restaurant",
                                "cafe",
                                "bar"
                            })
                    String group,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(
                storeService.getUserLikesStore(
                        campusIdx,
                        PageRequest.of(offset, limit),
                        group,
                        authenticatedUser.getIdx()));
    }

    @Auth(require = true)
    @PostMapping("/likes/{storeIdx}")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "인증된 사용자의 가게 저장 및 저장해제", description = "가게를 저장 혹은 저장을 해제합니다.")
    @ApiResponse(
            responseCode = "404",
            description = """
      (U0001)유저를 찾을 수 없습니다.
      (S0001)등록된 가게가 아닙니다.
      """,
            content = @Content(schema = @Schema()))
    public ApplicationResponse<Boolean> likesStore(
            @PathVariable(value = "storeIdx")
                    @Schema(title = "가게 키 값", description = "가게 키 값", example = "386")
                    Long storeIdx,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(
                storeService.likesStore(storeIdx, authenticatedUser.getIdx()));
    }

    @Auth(require = false)
    @GetMapping("/{campusIdx}/{keyword}")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "주변 식당 키워드 검색 API", description = "주변 식당 키워드 검색합니다")
    public ApplicationResponse<Page<StoreGetRes>> getStoresKeyword(
            @Schema(title = "캠퍼스 키 값", description = "캠퍼스 키 값", example = "1")
                    @PathVariable(value = "campusIdx")
                    Long campusIdx,
            @Schema(title = "키워드", description = "키워드", example = "치킨")
                    @PathVariable(value = "keyword")
                    String keyword,
            @Parameter(hidden = true) AuthenticatedUser authenticatedUser,
            @RequestParam(value = "offset", defaultValue = "0")
                    @Schema(title = "페이지 번호", example = "0", description = "페이지 번호는 0부터 시작합니다.")
                    Integer offset,
            @RequestParam(value = "limit", defaultValue = "10")
                    @Schema(
                            title = "Data 갯수",
                            example = "10",
                            description = "한 페이지에 보여지는 데이터 수 입니다.")
                    Integer limit) {
        return ApplicationResponse.ok(
                storeService.getStoresKeyword(
                        campusIdx,
                        keyword,
                        authenticatedUser.getIdx(),
                        PageRequest.of(offset, limit)));
    }

    @Auth(require = false)
    @GetMapping("/{index}")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "식당 상세 조회", description = "식당 상세 정보를 조회합니다")
    public ApplicationResponse<StoreGetRes> getStore(
            @PathVariable(value = "index")
                    @Schema(title = "식당 키 값", description = "식당 키 값", example = "1")
                    Long storeIdx,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(
                storeService.getStore(
                        storeIdx, authenticatedUser == null ? null : authenticatedUser.getIdx()));
    }

    @Auth(require = false)
    @GetMapping("/{index}/reviews")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "식당 리뷰 조회", description = "식당 리뷰를 조회합니다")
    public ApplicationResponse<Page<StoreGetReviewRes>> getStoreReview(
            @PathVariable(value = "index")
                    @Schema(title = "식당 키 값", description = "식당 키 값", example = "1")
                    Long storeIdx,
            @Parameter(hidden = true) @AuthUser AuthenticatedUser authenticatedUser,
            @RequestParam(value = "offset", defaultValue = "0")
                    @Schema(title = "페이지 번호", example = "0", description = "페이지 번호는 0부터 시작합니다.")
                    Integer offset,
            @RequestParam(value = "limit", defaultValue = "10")
                    @Schema(
                            title = "Data 갯수",
                            example = "10",
                            description = "한 페이지에 보여지는 데이터 수 입니다.")
                    Integer limit) {
        return ApplicationResponse.ok(
                storeService.getStoreReview(
                        storeIdx,
                        authenticatedUser == null ? null : authenticatedUser.getIdx(),
                        PageRequest.of(offset, limit)));
    }

    @GetMapping("/reviews")
    @SecurityRequirement(name = "jwt-user-auth")
    @Operation(summary = "식당 리뷰 조회", description = "식당 리뷰를 조회합니다")
    public ApplicationResponse<Page<StoresGetReviews>> getStoreReviews(
            @RequestParam(value = "offset", defaultValue = "0")
                    @Schema(title = "페이지 번호", example = "0", description = "페이지 번호는 0부터 시작합니다.")
                    Integer offset,
            @RequestParam(value = "limit", defaultValue = "10")
                    @Schema(
                            title = "Data 갯수",
                            example = "10",
                            description = "한 페이지에 보여지는 데이터 수 입니다.")
                    Integer limit,
            @RequestParam(value = "order")
                    @Schema(
                            title = "정렬 기준",
                            description = "정렬 기준은 기획에 따라 변경 가능합니다.",
                            allowableValues = {
                                SORT_NAME,
                                SORT_DISTANCE,
                                SORT_RECOMMENDEDCNOUNT,
                                SORT_REVIEWCOUNT,
                                SORT_GRADE,
                                SORT_RECENT,
                                SORT_REVIEWMARKCOUNT
                            })
                    String order,
            @RequestParam(value = "group", required = false, defaultValue = "all")
                    @Schema(
                            title = "그룹",
                            description = "그룹",
                            allowableValues = {
                                "all",
                                "etc",
                                "recommend",
                                "restaurant",
                                "cafe",
                                "bar",
                                "korean",
                                "chinese",
                                "japanese",
                                "western",
                            })
                    String group,
            @RequestParam(value = "grade", required = false)
                    @Schema(
                            title = "평점",
                            description = "평점",
                            allowableValues = {"1", "2", "3", "4", "5"})
                    Integer grade,
            @RequestParam(value = "campusIdx", required = false)
                    @Schema(title = "캠퍼스 키 값", description = "캠퍼스 키 값", example = "1")
                    Long campusIdx) {
        return ApplicationResponse.ok(
                storeService.getStoresReviews(
                        PageRequest.of(offset, limit), order, group, grade, campusIdx));
    }
}
