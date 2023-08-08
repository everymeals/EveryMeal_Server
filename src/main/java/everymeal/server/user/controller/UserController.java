package everymeal.server.user.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/meals")
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 관련 API입니다")
public class UserController {}
