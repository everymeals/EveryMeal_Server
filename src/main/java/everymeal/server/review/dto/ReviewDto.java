package everymeal.server.review.dto;

import java.util.Map;

public class ReviewDto {

  public record ReviewTodayGetRes(
      Long reviewIdx,
      String content
  ){}

  public static ReviewTodayGetRes of(Map<String, Object> resultMap) {
    return new ReviewTodayGetRes(
        (Long) resultMap.get("reviewIdx"),
        (String) resultMap.get("content")
    );
  }

}
