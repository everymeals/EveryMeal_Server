package everymeal.server.meal.entity;


import everymeal.server.review.entity.Review;
import everymeal.server.store.entity.GradeStatistics;
import everymeal.server.university.entity.University;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 20, nullable = false)
    private String name;

    private String address;
    private Boolean isDeleted = false;

    private Boolean isOpenBreakfast = true;
    private Boolean isOpenLunch = true;
    private Boolean isOpenDinner = true;

    private LocalTime breakfastStartTime = LocalTime.of(8, 0);
    private LocalTime breakfastEndTime = LocalTime.of(10, 30);
    private LocalTime lunchStartTime = LocalTime.of(11, 00);
    private LocalTime lunchEndTime = LocalTime.of(14, 30);
    private LocalTime dinnerStartTime = LocalTime.of(17, 00);
    private LocalTime dinnerEndTime = LocalTime.of(18, 30);

    @Embedded private GradeStatistics gradeStatistics;

    @ManyToOne private University university;

    @OneToMany(mappedBy = "restaurant")
    private Set<Review> reviews;

    @Builder
    public Restaurant(
            String name,
            String address,
            University university,
            LocalTime breakfastStartTime,
            LocalTime breakfastEndTime,
            LocalTime lunchStartTime,
            LocalTime lunchEndTime,
            LocalTime dinnerStartTime,
            LocalTime dinnerEndTime) {
        this.name = name;
        this.address = address;
        this.isDeleted = Boolean.TRUE;
        this.university = university;
        this.isOpenBreakfast = Boolean.TRUE;
        this.isOpenLunch = Boolean.TRUE;
        this.isOpenDinner = Boolean.TRUE;
        this.breakfastStartTime = breakfastStartTime;
        this.breakfastEndTime = breakfastEndTime;
        this.lunchStartTime = lunchStartTime;
        this.lunchEndTime = lunchEndTime;
        this.dinnerStartTime = dinnerStartTime;
        this.dinnerEndTime = dinnerEndTime;
        this.gradeStatistics = new GradeStatistics();
    }

    /** 학생식당 미운영 상태로 변경 폐업, 업체 변경 등의 이유일 경우, 해당 함수를 통해 상태 변경 */
    public void updateUseYn() {
        this.isDeleted = false;
    }

    public void updateIsOpenBreakfast() {
        this.isOpenBreakfast = !this.isOpenBreakfast;
    }

    public void updateIsOpenLunch() {
        this.isOpenLunch = !this.isOpenLunch;
    }

    public void updateIsOpenDinner() {
        this.isOpenDinner = !this.isOpenDinner;
    }

    public void updateBreakfastStartTime(LocalTime breakfastStartTime) {
        this.breakfastStartTime = breakfastStartTime;
    }

    public void updateBreakfastEndTime(LocalTime breakfastEndTime) {
        this.breakfastEndTime = breakfastEndTime;
    }

    public void updateLunchStartTime(LocalTime lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public void updateLunchEndTime(LocalTime lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public void updateDinnerStartTime(LocalTime dinnerStartTime) {
        this.dinnerStartTime = dinnerStartTime;
    }

    public void updateDinnerEndTime(LocalTime dinnerEndTime) {
        this.dinnerEndTime = dinnerEndTime;
    }

    public void updateRestaurant(
            String name,
            String address,
            LocalTime breakfastStartTime,
            LocalTime breakfastEndTime,
            LocalTime lunchStartTime,
            LocalTime lunchEndTime,
            LocalTime dinnerStartTime,
            LocalTime dinnerEndTime) {
        this.name = name;
        this.address = address;
        this.breakfastStartTime = breakfastStartTime;
        this.breakfastEndTime = breakfastEndTime;
        this.lunchStartTime = lunchStartTime;
        this.lunchEndTime = lunchEndTime;
        this.dinnerStartTime = dinnerStartTime;
        this.dinnerEndTime = dinnerEndTime;
    }

    public void addGrade(int grade) {
        this.gradeStatistics.addGrade(grade);
    }

    public void changeGrade(int oldGrade, int newGrade) {
        this.gradeStatistics.changeGrade(oldGrade, newGrade);
    }

    public void removeGrade(int grade) {
        this.gradeStatistics.removeGrade(grade);
    }
}
