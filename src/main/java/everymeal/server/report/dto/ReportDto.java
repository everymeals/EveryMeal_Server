package everymeal.server.report.dto;


import everymeal.server.report.entity.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ReportDto {

    public record ReportReviewReq(
            @Schema(description = "리뷰 신고 사유를 입력해주세요.") @NotNull(message = "리뷰 신고 사유를 입력해주세요.")
                    ReportReason reason) {}
}
