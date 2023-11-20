package everymeal.server.review.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.store.entity.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String imageUrl;

    @ManyToOne private Store store;

    private Boolean isDeleted;

    @Builder
    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
        isDeleted = false;
    }
}
