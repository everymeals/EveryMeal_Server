package everymeal.server.global.util.kakaobatch;


import everymeal.server.store.entity.Store;
import everymeal.server.university.entity.University;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoStoreVO {
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private int distance;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String x;
    private String y;

    public Store of(University university) {
        return Store.builder()
                .name(place_name)
                .address(address_name)
                .category(category_name)
                .categoryGroup(category_group_name)
                .kakaoId(id)
                .phone(phone)
                .url(place_url)
                .roadAddress(road_address_name)
                .grade(0.0)
                .recommendedCount(0)
                .distance(distance)
                .reviewCount(0)
                .x(x)
                .y(y)
                .university(university)
                .build();
    }
}
