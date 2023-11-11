package everymeal.server.global.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth bearer token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info =
                new Info()
                        .title("EveryMeal API Doc")
                        .version("v0.0.1")
                        .description(
                                """
                            EveryMeal API 명세서입니다.<br>
                            스웨거 한계로 인해 Response에 공통 response의 data에 대한 정의가 되어있습니다. <br>""");
        return new OpenAPI().components(new Components()).info(info);
    }
}
