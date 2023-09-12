package everymeal.server.meal.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantRegisterReq {
    @Schema(description = "학교 ENUM 값", defaultValue = "MYONGJI_S")
    private String university;

    @Schema(description = "학교 주소", defaultValue = "서울시 서대문구 남가좌동 거북골로 34")
    private String address;

    @Schema(description = "학생식당 한글명", defaultValue = "MCC 식당")
    private String name;
}
