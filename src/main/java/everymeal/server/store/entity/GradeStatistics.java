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
}
