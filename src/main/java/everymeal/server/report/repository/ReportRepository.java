package everymeal.server.report.repository;


import everymeal.server.report.entity.Report;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(
            value =
                    "select r from reports r where r.review.idx = :reviewIdx and r.reporter.idx = :userIdx")
    Optional<Report> findByReviewIdxAndUserIdx(
            @Param("reviewIdx") Long reviewIdx, @Param("userIdx") Long userIdx);
}
