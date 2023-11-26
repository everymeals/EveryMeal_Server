package everymeal.server.user.entity;


import everymeal.server.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdrawal extends BaseEntity {
    @Id private Long userIdx;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WithdrawalReason withdrawalReason;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String etcReason;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_idx", referencedColumnName = "idx")
    private User user;

    @Builder
    public Withdrawal(WithdrawalReason withdrawalReason, String etcReason, User user) {
        this.withdrawalReason = withdrawalReason;
        this.etcReason = etcReason;
        this.user = user;
    }
}
