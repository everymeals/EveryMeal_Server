package everymeal.server.global.util.aws.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.aws.S3Util;
import everymeal.server.global.util.aws.controller.dto.S3GetResignedUrlRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/s3")
@RestController
@RequiredArgsConstructor
@Tag(name = "Image S3 API", description = "Image S3 API입니다")
public class S3Controller {

    private final S3Util s3Util;

    @GetMapping("/presigned-url")
    @Operation(
            summary = "S3에 이미지를 저장하기 위한 PresignedUrl을 반환합니다.",
            description =
                    """
        1. 해당 API를 호출하면, Response에 url과 imageKey를 반환합니다.
        2. Url은 2분간 유효합니다.
        3. ImageKey는 S3에 저장될 파일의 이름입니다.
        4. Url에 PutMapping 이미지 파일을 binary로 보낼 경우, S3에 저장됩니다.
        PS. Server에는 ImageKey로만 통신합합니다.
        """)
    public ApplicationResponse<List<S3GetResignedUrlRes>> getPresignedUrl(
            @RequestParam(value = "fileDomain")
                    @Schema(
                            title = "파일 도메인",
                            defaultValue = "store",
                            example = "store",
                            description = "store, meal, user 중 하나를 입력해주세요.",
                            allowableValues = {"store", "meal", "user"})
                    String fileDomain,
            @Min(value = 1, message = "최소 1개 이상의 파일을 업로드해주세요.")
            @Max(value = 10, message = "최대 10개까지 업로드 가능합니다.")
            @RequestParam(value = "count")
                    @Schema(
                            title = "파일 개수",
                            defaultValue = "1",
                            example = "1",
                            description = "파일 개수를 입력해주세요. 최대 10개까지 업로드 가능합니다.")
                    Integer count) {
        List<S3GetResignedUrlRes> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String fileName = UUID.randomUUID().toString();
            fileName = fileDomain + File.separator + fileName;
            URL url = s3Util.getPresignedUrl(fileName);
            S3GetResignedUrlRes build = S3GetResignedUrlRes.builder().imageKey(fileName)
                .url(url.toString()).build();
            res.add(build);
        }
        return ApplicationResponse.ok(res);
    }

    @Operation(
            summary = "S3 이미지 삭제",
            description =
                    """
        S3에 저장된 이미지를 삭제합니다.
        언제 사용하는 API인가요?
        1. 리뷰 작성 중 이미지를 업로드하고, 리뷰 작성을 취소할 때
        2. 리뷰 수정 중 이미지를 업로드하고, 리뷰 수정을 취소할 때
        3. 리뷰 삭제 시 이미지를 삭제할 때
        4. 이미지를 잘못 업로드 했을 때
        5. 유저 프로필 이미지를 변경할 때 ( 기본 이미지 제외 )
""")
    @DeleteMapping("/image")
    public ApplicationResponse<Void> deleteImage(
            @RequestParam(value = "fileName") String fileName) {
        s3Util.deleteImage(fileName);
        return ApplicationResponse.ok();
    }
}
