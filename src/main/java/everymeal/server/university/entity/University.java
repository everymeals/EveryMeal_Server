package everymeal.server.university.entity;


import everymeal.server.global.entity.BaseEntity;
import jakarta.persistence.Entity;
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
public class University extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;

    private String campusName;

    private Boolean isDeleted;

    @Builder
    public University(Long idx, String name, String campusName, Boolean isDeleted) {
        this.idx = idx;
        this.name = name;
        this.campusName = campusName;
        this.isDeleted = Boolean.FALSE;
    }
}
