package everymeal.server.report.service;


import everymeal.server.report.entity.Report;
import everymeal.server.report.repository.ReportRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportCommServiceImpl {

    private final ReportRepository reportRepository;

    @Transactional
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public Optional<Report> getReportOptionalEntity(Long reviewIdx, Long userIdx) {
        return reportRepository.findByReviewIdxAndUserIdx(reviewIdx, userIdx);
    }
}
