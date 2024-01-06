package everymeal.server.report.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ReportReason {
    UNRELATED("해당 식당과 무관한 리뷰"),
    PROFANITY("비속어 및 혐오 발언"),
    PORNOGRAPHY("음란성 게시물"),
    ETC("기타");

    private String value;

    ReportReason(String value) {
        this.value = value;
    }
}
