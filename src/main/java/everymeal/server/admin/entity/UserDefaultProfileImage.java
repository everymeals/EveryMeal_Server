package everymeal.server.admin.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Table(catalog = "admin", name = "user_default_profile_image")
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserDefaultProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String profileImgUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
