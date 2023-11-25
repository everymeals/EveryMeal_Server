package everymeal.server.user.controller.dto.request;


import everymeal.server.user.entity.WithdrawalReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record WithdrawalReq(
        @NotBlank @Schema(description = "탈퇴 사유를 입력해주세요.", example = "NOT_USE_USUALLY")
                WithdrawalReason withdrawalReason,
        @Schema(description = "사유가 '기타'일 경우, 추가 이유 입력해주세요.", example = "다른 서비스를 사용하게 되었다.")
                String etcReason) {}
