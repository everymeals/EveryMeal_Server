package everymeal.server.meal.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantRegisterReq {
    @Schema(title = "대학 이름", description = "학교 한글명", defaultValue = "명지대학교")
    @NotBlank
    private String universityName;

    @Schema(title = "캠퍼스 이름", description = "학교 캠퍼스명", defaultValue = "인문캠퍼스")
    @NotBlank
    private String campusName;

    @Schema(
            title = "학교 주소",
            description = "시/구/동 도로명 주소 모두 기입",
            defaultValue = "서울시 서대문구 남가좌동 거북골로 34")
    @NotBlank
    private String address;

    @Schema(title = "학생 식당 이름", description = "학생식당 한글명", defaultValue = "MCC 식당")
    @NotBlank
    private String restaurantName;
}
