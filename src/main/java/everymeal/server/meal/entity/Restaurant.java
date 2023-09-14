package everymeal.server.meal.entity;


import everymeal.server.university.entity.University;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne private University university;

    @Builder
    public Restaurant(String name, String address, Boolean useYn, University university) {
        this.name = name;
        this.address = address;
        this.useYn = useYn;
        this.university = university;
    }

    /** 학생식당 미운영 상태로 변경 폐업, 업체 변경 등의 이유일 경우, 해당 함수를 통해 상태 변경 */
    public void updateUseYn() {
        this.useYn = false;
    }
}
