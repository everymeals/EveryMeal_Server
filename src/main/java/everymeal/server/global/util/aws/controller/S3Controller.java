package everymeal.server.global.util.aws.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.aws.S3Util;
import everymeal.server.global.util.aws.controller.dto.S3GetResignedUrlRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
    public ApplicationResponse<S3GetResignedUrlRes> getPresignedUrl(
            @RequestParam(value = "fileDomain")
                    @Schema(
                            title = "파일 도메인",
                            defaultValue = "store",
                            example = "store",
                            description = "store, meal, user 중 하나를 입력해주세요.",
                            allowableValues = {"store", "meal", "user"})
                    String fileDomain) {
        String fileName = UUID.randomUUID().toString();
        fileName = fileDomain + File.separator + fileName;
        URL test = s3Util.getPresignedUrl(fileName);
        return ApplicationResponse.ok(
                S3GetResignedUrlRes.builder().imageKey(fileName).url(test.toString()).build());
    }
}
