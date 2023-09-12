package everymeal.server.user.service;


import everymeal.server.user.entity.Users;
import everymeal.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Boolean signUp(String userDeviceId) {
        Users user = Users.builder().deviceId(userDeviceId).build();
        userRepository.save(user);
        return true;
    }
}
