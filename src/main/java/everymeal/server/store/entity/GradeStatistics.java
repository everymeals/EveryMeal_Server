package everymeal.server.store.entity;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;

@Embeddable
@Getter
public class GradeStatistics implements Serializable {
    private Double grade;
    private Integer reviewCount;
    private Integer recommendedCount;

    public GradeStatistics() {
        this.grade = 0.0;
        this.reviewCount = 0;
        this.recommendedCount = 0;
    }

    /** 갱신된 총 리뷰 수 = 이전 총 리뷰 수+1 갱신된 총 평점 = ( 이전 총 평점 × 이전 총 리뷰 수 ) + 새로운 리뷰 평점 / 갱신된 총 리뷰 수 */
    public void addGrade(int grade) {
        this.grade = (this.grade * this.reviewCount + grade) / (this.reviewCount + 1);
        this.reviewCount++;
    }

    /**
     * 갱신된 총 리뷰 수 = 이전 총 리뷰 수 갱신된 총 평점 = ( 이전 총 평점 × 이전 총 리뷰 수 ) - 이전 리뷰 평점 + 새로운 리뷰 평점 / 갱신된 총 리뷰 수
     */
    public void changeGrade(int oldGrade, int newGrade) {
        if (this.reviewCount != 0)
            this.grade = (this.grade * this.reviewCount - oldGrade + newGrade) / this.reviewCount;
    }

    /** 갱신된 총 리뷰 수 = 이전 총 리뷰 수-1 갱신된 총 평점 = ( 이전 총 평점 × 이전 총 리뷰 수 ) - 이전 리뷰 평점 / 갱신된 총 리뷰 수 */
    public void removeGrade(int grade) {
        this.grade = (this.grade * this.reviewCount - grade) / (this.reviewCount - 1);
        this.reviewCount--;
    }

    public void addRecommendedCount() {
        this.recommendedCount++;
    }

    public void removeRecommendedCount() {
        this.recommendedCount--;
    }
}
