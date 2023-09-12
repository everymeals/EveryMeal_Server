package everymeal.server.meal.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MealRegisterReq {

    @Schema(description = "메뉴를 ',' 구분자를 기준으로 묶어서 하나의 문자열로 보내주세요.", defaultValue = "갈비탕, 깍두기, 흰쌀밥")
    private String menu;

    @Schema(description = "식사 분류 ( 조식 | 중식 | 석식 | 특식 ) ENUM으로 관리합니다.", defaultValue = "LUNCH")
    private String mealType;

    @Schema(description = "식사 운영 상태 ( 운영 | 미운영 | 단축운영 )", defaultValue = "OPEN")
    private String mealStatus;

    @Schema(description = "식사 제공 날짜 ( yyyy-MM-dd ) ", defaultValue = "2023-10-01")
    private LocalDate offeredAt;

    @Schema(description = "가격을 Double 형태로 관리합니다.", defaultValue = "10000.0")
    private Double price;
}
