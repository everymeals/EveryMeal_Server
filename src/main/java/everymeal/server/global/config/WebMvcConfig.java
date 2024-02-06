package everymeal.server.global.config;


import everymeal.server.global.util.authresolver.UserJwtResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserJwtResolver adminResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(adminResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "dev.everymeal.shop:8085"
            ) // 허용할 출처
            .allowedMethods("*") // 허용할 HTTP method
            .allowedHeaders("*") // 허용할 HTTP Header
            .allowCredentials(true) // 쿠키 인증 요청 허용
            .maxAge(60 * 60 * 24 * 30L); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}
