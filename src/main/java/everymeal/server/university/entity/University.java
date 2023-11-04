package everymeal.server.university.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.store.entity.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<Store> stores = new ArrayList<>();

    @Builder
    public University(String name, String campusName) {
        this.name = name;
        this.campusName = campusName;
        this.isDeleted = Boolean.FALSE;
    }
}
