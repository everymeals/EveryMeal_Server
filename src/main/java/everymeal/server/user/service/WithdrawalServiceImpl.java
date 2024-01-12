package everymeal.server.user.service;


import everymeal.server.user.entity.Withdrawal;
import everymeal.server.user.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawalServiceImpl {

    private final WithdrawalRepository withdrawalRepository;

    @Transactional
    public void save(Withdrawal withdrawal) {
        withdrawalRepository.save(withdrawal);
    }
}
