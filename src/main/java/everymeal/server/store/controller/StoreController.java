package everymeal.server.store.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/stores")
@RestController
@RequiredArgsConstructor
@Tag(name = "Store API", description = "학교 주변 식당 관련 API입니다")
public class StoreController {
    private final StoreService storeService;

    @GetMapping()
    @Operation(summary = "학교별 식당 목록 조회", description = "학교별 식당 목록을 조회합니다")
    public ApplicationResponse<Map> getStores(
            @RequestParam(value = "universityName") String universityName,
            @RequestParam(value = "campusName") String campusName) {
        return ApplicationResponse.create(null);
    }
}
