package everymeal.server.review;

import everymeal.server.meal.MealData;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewGetRes;
import everymeal.server.review.dto.ReviewPaging;
import everymeal.server.review.dto.ReviewPagingVOWithCnt;
import everymeal.server.review.entity.Review;
import everymeal.server.university.entity.University;
import everymeal.server.user.entity.User;
import java.util.List;

public class ReviewData {

  public static ReviewCreateReq getReviewCreateRequest() {
    return new ReviewCreateReq(
        1L,
      1,
      "리뷰 내용",
        List.of(),
        true
    );
  }

  public static User getUserEntity() {
    return new User(
        "test",
        "test@everymeal.com",
        "test_IMG_URL",
        MealData.getUniversity()
    );
  }

  public static User getUserEntity(University university) {
    return new User(
        "test",
        "test@everymeal.com",
        "test_IMG_URL",
        university
    );
  }

  public static User getUserEntity(University university, int i) {
    return new User(
        "test" + i,
        "test" + i + "@everymeal.com",
        "test_IMG_URL",
        university
    );
  }

  public static Review getReviewEntity() {
    return Review.builder()
        .restaurant(MealData.getRestaurant())
        .grade(1)
        .images(List.of())
        .content("리뷰 내용")
        .user(getUserEntity())
        .build();
  }

  public static Review getReviewEntity(Restaurant restaurant, User user) {
    return Review.builder()
        .restaurant(restaurant)
        .grade(1)
        .images(List.of())
        .content("리뷰 내용")
        .user(user)
        .build();
  }

  public static ReviewGetRes getReviewGetRes() {
    return new ReviewGetRes(
        1,
        List.of(new ReviewPaging(
            1L,
            "식당 이름",
            "닉네임",
            "프로필 이미지",
            true,
            1,
            "리뷰 내용",
            List.of("이미지 주소"),
            1
        )));
  }

  public static ReviewPaging getReviewPaging() {
    return new ReviewPaging(
        1L,
        "식당 이름",
        "닉네임",
        "프로필 이미지",
        true,
        1,
        "리뷰 내용",
        List.of("이미지 주소"),
        1
    );
  }

  public static ReviewPagingVOWithCnt getReviewPagingVOWithCnt() {
    return new ReviewPagingVOWithCnt(
        1,
        List.of(getReviewEntity(),
            getReviewEntity(),
            getReviewEntity(),
            getReviewEntity(),
            getReviewEntity(),
            getReviewEntity(),
            getReviewEntity(),
            getReviewEntity())
        );
  }

}
