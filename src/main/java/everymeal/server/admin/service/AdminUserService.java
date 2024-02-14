package everymeal.server.admin.service;


import everymeal.server.admin.dto.AdminUserDto.DefaultProfileImageRes;
import everymeal.server.admin.repository.UserDefaultProfileImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserDefaultProfileImageRepository userDefaultProfileImageRepository;

    public List<DefaultProfileImageRes> getDefaultProfileImages() {
        return userDefaultProfileImageRepository.findAll().stream()
                .map(DefaultProfileImageRes::of)
                .toList();
    }
}
