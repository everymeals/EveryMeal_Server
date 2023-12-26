package everymeal.server.meal.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record RestaurantRegisterReq(
        @Schema(title = "대학 이름", description = "학교 한글명", defaultValue = "명지대학교") @NotBlank
                String universityName,
        @Schema(title = "캠퍼스 이름", description = "학교 캠퍼스명", defaultValue = "인문캠퍼스") @NotBlank
                String campusName,
        @Schema(
                        title = "학교 주소",
                        description = "시/구/동 도로명 주소 모두 기입",
                        defaultValue = "서울시 서대문구 남가좌동 거북골로 34")
                @NotBlank
                String address,
        @Schema(title = "학생 식당 이름", description = "학생식당 한글명", defaultValue = "MCC 식당") @NotBlank
                String restaurantName,
        @Schema(title = "조식 시작 시간", description = "조식 시작 시간", defaultValue = "08:00")
                @NotNull
                @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
                LocalTime breakfastStartTime,
        @Schema(title = "조식 종료 시간", description = "조식 종료 시간", defaultValue = "10:30")
                @NotNull
                @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
                LocalTime breakfastEndTime,
        @Schema(title = "중식 시작 시간", description = "중식 시작 시간", defaultValue = "11:00")
                @NotNull
                @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
                LocalTime lunchStartTime,
        @Schema(title = "중식 종료 시간", description = "중식 종료 시간", defaultValue = "14:30")
                @NotNull
                @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
                LocalTime lunchEndTime,
        @Schema(title = "석식 시작 시간", description = "석식 시작 시간", defaultValue = "17:00")
                @NotNull
                @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
                LocalTime dinnerStartTime,
        @Schema(title = "석식 종료 시간", description = "석식 종료 시간", defaultValue = "18:30")
                @NotNull
                @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
                LocalTime dinnerEndTime) {}
