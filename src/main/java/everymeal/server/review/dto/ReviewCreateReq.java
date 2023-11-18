package everymeal.server.review.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ReviewCreateReq(
        @Schema(description = "리뷰를 남기고자 하는 학식의 idx를 입력해주세요.", defaultValue = "1") @NotBlank
                Long mealIdx,
        @Schema(description = "학식에 대한 리뷰 평가 점수를 정수형 최소 1 ~ 최대 5점까지로 입력해주세요.", defaultValue = "5")
                @NotBlank
                int grade,
        @Schema(
                        description = "학식에 대한 리뷰 내용을 1글자 이상 300자 내로 입력해주세요.",
                        defaultValue = "오늘 학식 진짜 미침...안먹으면 땅을 치고 후회함")
                @NotBlank
                @Size(min = 1, max = 300)
                String content,
        @Schema(
                        description = "학식이 보이는 사진 이미지 주소를 String 리스트 형태로 입력해주세요.",
                        defaultValue = "['이미지 주소']")
                List<String> imageList) {}
