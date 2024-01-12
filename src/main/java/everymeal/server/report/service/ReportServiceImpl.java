package everymeal.server.report.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.report.dto.ReportDto.ReportReviewReq;
import everymeal.server.report.entity.Report;
import everymeal.server.review.entity.Review;
import everymeal.server.review.service.ReviewCommServiceImpl;
import everymeal.server.user.entity.User;
import everymeal.server.user.service.UserCommServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReviewCommServiceImpl reviewCommServiceImpl;
    private final UserCommServiceImpl userCommServiceImpl;
    private final ReportCommServiceImpl reportCommServiceImpl;

    @Override
    @Transactional
    public Boolean reportReview(Long reviewIdx, ReportReviewReq request, Long userIdx) {
        // 리뷰 조회
        Review review = reviewCommServiceImpl.getReviewEntity(reviewIdx);
        // 신고 유저 조회
        User reportUser = userCommServiceImpl.getUserEntity(userIdx);
        // 리뷰 작성 유저 조회
        User reviewUser = review.getUser();
        // 리뷰 작성 유저와 신고 유저가 같은지 확인
        if (reviewUser.equals(reportUser)) {
            throw new ApplicationException(ExceptionList.REPORT_REVIEW_SELF);
        }
        // 리뷰 신고 여부 확인
        if (reportCommServiceImpl.getReportOptionalEntity(reviewIdx, userIdx).isPresent()) {
            throw new ApplicationException(ExceptionList.REPORT_REVIEW_ALREADY);
        }
        // 리뷰 신고 저장
        Report report =
                Report.builder()
                        .reporter(reportUser)
                        .review(review)
                        .reason(request.reason())
                        .build();
        reportCommServiceImpl.save(report);

        return true;
    }
}
