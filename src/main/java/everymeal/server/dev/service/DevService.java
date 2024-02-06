package everymeal.server.dev.service;


import everymeal.server.dev.controller.dto.response.LoginRes;
import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.aws.S3Util;
import everymeal.server.user.entity.User;
import everymeal.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LoginRes userLogin(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ApplicationException("해당 유저가 없습니다."));
        String accessToken = jwtUtil.generateAccessToken(user.getIdx());
        String refreshToken = jwtUtil.generateRefreshToken(user.getIdx(), accessToken);
        return new LoginRes(
                accessToken,
                user.getNickname(),
                S3Util.getImgUrl(user.getProfileImgUrl()),
                refreshToken);
    }
}
