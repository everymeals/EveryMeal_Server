package everymeal.server.user.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCommServiceImpl {

    private final UserRepository userRepository;

    public User getUserEntity(Long userIdx) {
        return userRepository
                .findById(userIdx)
                .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
    }

    public Optional<User> getUserOptionalEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserEntityByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
    }

    public Optional<User> getUserOptionalEntityByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public User getUserEntityByNickname(String nickname) {
        return userRepository
                .findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(ExceptionList.USER_NOT_FOUND));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
