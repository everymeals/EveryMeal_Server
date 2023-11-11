package everymeal.server.review.service;


import everymeal.server.review.dto.ReviewCreateReq;
import everymeal.server.review.dto.ReviewGetRes;
import everymeal.server.user.entity.User;

public interface ReviewService {
    Boolean createReview(ReviewCreateReq request, User user);

    Boolean updateReview(ReviewCreateReq request, User user, Long reviewIdx);

    Boolean deleteReview(User user, Long reviewIdx);

    ReviewGetRes getReviewWithNoOffSetPaging(Long cursorIdx, Long mealIdx, int pageSize);
}
