package everymeal.server.store.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.store.controller.dto.response.StoreGetRes;
import everymeal.server.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/stores")
@RestController
@RequiredArgsConstructor
@Tag(name = "Store API", description = "학교 주변 식당 관련 API입니다")
public class StoreController {

    private final StoreService storeService;

    /**
     * ============================================================================================
     * GLOBAL STATIC CONSTANTS
     * =============================================================================================
     */
    final String SORT_NAME = "name";

    final String SORT_DISTANCE = "distance";
    final String SORT_RECOMMENDEDCNOUNT = "recommendedCount";
    final String SORT_REVIEWCOUNT = "reviewCount";
    final String SORT_GRADE = "grade";

    @GetMapping("/{campusIdx}")
    @Operation(summary = "학교 주변 식당 조회", description = "학교 주변 식당을 조회합니다")
    public ApplicationResponse<Page<StoreGetRes>> getStores(
            @PathVariable(value = "campusIdx")
                    @Schema(title = "캠퍼스 키 값", description = "캠퍼스 키 값", example = "1")
                    Long campusIdx,
            @RequestParam(value = "offset", defaultValue = "0")
                    @Schema(title = "페이지 번호", example = "0", description = "0부터 시작합니다")
                    Integer offset,
            @RequestParam(value = "limit", defaultValue = "10")
                    @Schema(
                            title = "Data 갯수",
                            example = "10",
                            description = "한 페이지에 보여지는 데이터 수 입니다.")
                    Integer limit,
            @RequestParam(value = "orderBy")
                    @Schema(
                            title = "정렬 기준",
                            description = "정렬 기준은 기획에 따라 변경 가능합니다.",
                            allowableValues = {
                                SORT_NAME,
                                SORT_DISTANCE,
                                SORT_RECOMMENDEDCNOUNT,
                                SORT_REVIEWCOUNT,
                                SORT_GRADE
                            })
                    String orderBy) {
        Sort sort = Sort.by(orderBy);
        sort =
                switch (orderBy) {
                    case SORT_NAME, SORT_DISTANCE -> sort.ascending();
                    case SORT_RECOMMENDEDCNOUNT, SORT_REVIEWCOUNT, SORT_GRADE -> sort.descending();
                    default -> Sort.by(orderBy);
                };
        return ApplicationResponse.ok(
                storeService.getStores(campusIdx, PageRequest.of(offset, limit, sort)));
    }
}
