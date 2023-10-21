package everymeal.server.global.util.aws.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class S3GetResignedUrlRes {

    private String imageKey;
    private String url;

}
