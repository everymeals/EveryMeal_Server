package everymeal.server.review.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.store.entity.Store;
import everymeal.server.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 300)
    private String content;

    @Column(nullable = false, length = 1)
    private int grade;

    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isTodayReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_idx")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_idx")
    private Store store;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private Set<ReviewMark> reviewMarks = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_idx")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Review(
            String content,
            int grade,
            List<Image> images,
            User user,
            Restaurant restaurant,
            Store store) {
        this.content = content;
        this.grade = grade;
        this.images = images;
        this.user = user;
        this.restaurant = restaurant;
        this.isDeleted = Boolean.FALSE;
        this.store = store;
        this.isTodayReview = Boolean.FALSE;
    }

    public void updateEntity(String content, int grade, List<Image> images, Boolean todayReview) {
        this.content = content;
        this.grade = grade;
        if (images != null) {
            this.images.clear();
        }
        this.images = images;
        this.isTodayReview = todayReview;
    }

    public void deleteEntity() {
        this.isDeleted = Boolean.TRUE;
    }

    public void updateTodayReview(boolean isTodayReview) {
        this.isTodayReview = isTodayReview;
    }

    public void addMark(User user) {
        ReviewMark reviewMark = ReviewMark.builder().review(this).user(user).build();
        this.reviewMarks.add(reviewMark);
    }

    public void removeMark(User user) {
        this.reviewMarks.removeIf(mark -> mark.getUser().getIdx().equals(user.getIdx()));
        System.out.println("size = " + this.reviewMarks.size());
        System.out.println(
                "remark = "
                        + this.reviewMarks.stream()
                                .anyMatch(mark -> mark.getUser().getIdx().equals(user.getIdx())));
    }

    public boolean isLike(Long userIdx) {
        if (this.reviewMarks.isEmpty()) {
            return false;
        } else
            return this.reviewMarks.stream()
                    .anyMatch(mark -> mark.getUser().getIdx().equals(userIdx));
    }
}
