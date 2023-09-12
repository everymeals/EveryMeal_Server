package everymeal.server.meal.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(value = "restaurant")
public class Restaurant {
    @Id private String _id;
    private String name;
    private String address;
    private boolean useYn;
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
