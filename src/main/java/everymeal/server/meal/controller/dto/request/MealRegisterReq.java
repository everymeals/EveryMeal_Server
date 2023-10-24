package everymeal.server.meal.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record MealRegisterReq(
        @Schema(
                        description = "메뉴를 ',' 구분자를 기준으로 묶어서 하나의 문자열로 보내주세요.",
                        defaultValue = "갈비탕, 깍두기, 흰쌀밥")
                @NotBlank
                String menu,
        @Schema(
                        description = "식사 분류 ( BREAKFAST | LUNCH | DINNER ) ENUM으로 관리합니다.",
                        defaultValue = "LUNCH")
                @NotBlank
                String mealType,
        @Schema(description = "식사 운영 상태 ( OPEN | CLOSED | SHORT_OPEN )", defaultValue = "OPEN")
                String mealStatus,
        @Schema(description = "식사 제공 날짜 ( yyyy-MM-dd ) ", defaultValue = "2023-10-01") @NotBlank
                LocalDate offeredAt,
        @Schema(description = "가격을 Double 형태로 관리합니다.", defaultValue = "0.0") Double price,
        @Schema(
                        description =
                                "음식의 카테고리 ( DEFAULT | KOREAN | JAPANESE | CHINESE | SNACKBAR | WESTERN ) 중 단일 메뉴라면 DEFAULT를 입력해주세요.",
                        defaultValue = "DEFAULT")
                @NotBlank
                String category) {}
