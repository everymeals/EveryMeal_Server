package everymeal.server.meal.entity;


import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String address;
    private Boolean useYn;

    @Enumerated(EnumType.STRING)
    private University university;

    @Builder
    public Restaurant(String name, String address, University university) {
        this.name = name;
        this.address = address;
        this.university = university;
        this.useYn = true;
    }

    /** 학생식당 미운영 상태로 변경 폐업, 업체 변경 등의 이유일 경우, 해당 함수를 통해 상태 변경 */
    public void updateUseYn() {
        this.useYn = false;
    }
}
