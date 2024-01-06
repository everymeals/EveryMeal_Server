package everymeal.server.report.service;


import everymeal.server.report.dto.ReportDto.ReportReviewReq;

public interface ReportService {

    Boolean reportReview(Long reviewIdx, ReportReviewReq request, Long userIdx);
}
