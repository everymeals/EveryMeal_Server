package everymeal.server.review.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ReviewCreateReq(
        @Schema(description = "리뷰를 남기고자 하는 식당의 idx를 입력해주세요.", defaultValue = "1") @NotBlank
                Long restaurantIdx,
        @Schema(
                        description =
                                "학식에 대한 리뷰 평가 점수를 정수형 최소 1 ~ 최대 5점까지로 입력해주세요. 사진 리뷰인 경우 0으로 보내주세요.",
                        defaultValue = "5")
                @Nullable
                Integer grade,
        @Schema(
                        description = "학식에 대한 리뷰 내용을 1글자 이상 입력해주세요. 사진 리뷰인 경우 null로 보내주세요.",
                        defaultValue = "오늘 학식 진짜 미침...안먹으면 땅을 치고 후회함")
                @Nullable
                String content,
        @Schema(
                        description =
                                "학식이 보이는 사진 이미지 주소를 String 리스트 형태로 입력해주세요. 최대 10개의 이미지를 첨부할 수 있습니다.",
                        defaultValue = "['이미지 주소']")
                @Max(10)
                List<String> imageList,
        @Schema(
                        description =
                                "오늘의 리뷰인지 아닌지를 boolean 형태로 입력해주세요. 오늘의 리뷰는 true, 그 외의 리뷰는 false로 입력해주세요.",
                        defaultValue = "true")
                Boolean isTodayReview) {}
