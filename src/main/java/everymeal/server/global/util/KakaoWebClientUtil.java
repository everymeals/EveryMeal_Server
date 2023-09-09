package everymeal.server.global.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoWebClientUtil {

    @Value("${kakao.key.rest-api-key}")
    private String restApiKey;

    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + restApiKey)
                .build();
    }
}
