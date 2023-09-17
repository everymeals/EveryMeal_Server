package everymeal.server.university.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.university.controller.dto.response.UniversityListGetRes;
import everymeal.server.university.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/universities")
@RestController
@RequiredArgsConstructor
@Tag(name = "University API", description = "대학 관련 API입니다")
public class UniversityController {

    private final UniversityService universityService;

    @Operation(summary = "대학 목록 조회")
    @GetMapping()
    public ApplicationResponse<List<UniversityListGetRes>> getUniversities(
            @Schema(title = "대학 이름", defaultValue = "명지대학교", example = "명지대학교")
                    @RequestParam(value = "universityName")
                    String universityName,
            @Schema(title = "캠퍼스 이름", defaultValue = "인문캠퍼스", example = "인문캠퍼스")
                    @RequestParam(value = "campusName")
                    String campusName) {
        return ApplicationResponse.ok(
                universityService.getUniversityList(universityName, campusName));
    }

    @Operation(summary = "대학 추가")
    @PostMapping()
    public ApplicationResponse<Boolean> addUniversity(
            @Schema(title = "대학 이름", defaultValue = "명지대학교", example = "명지대학교")
                    @RequestParam(value = "universityName")
                    String universityName,
            @Schema(title = "캠퍼스 이름", defaultValue = "인문캠퍼스", example = "인문캠퍼스")
                    @RequestParam(value = "campusName")
                    String campusName) {
        return ApplicationResponse.ok(universityService.addUniversity(universityName, campusName));
    }
}
