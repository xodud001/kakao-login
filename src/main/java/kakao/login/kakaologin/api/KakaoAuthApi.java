package kakao.login.kakaologin.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.login.kakaologin.api.response.GetTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoAuthApi {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String REST_API_APP_KEY = "6971f0ce92ee7c72df40efd0dc64fec7";
    private static final String CLIENT_SECRET = "mdWizUsYCJGYeLvJSvxeg4eVK6k1yS39";

    private static final String GET_TOKEN_URL = "/oauth/token";



    public KakaoAuthApi(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder()
                .baseUrl("http://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    public GetTokenResponse getToken(String code, String redirectUrl){
        webClient.post()
                .uri(GET_TOKEN_URL)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", REST_API_APP_KEY)
                        .with("redirect_uri", redirectUrl)
                        .with("code", code)
                        .with("client_secret", CLIENT_SECRET)
                ).retrieve()
                .bodyToMono(String.class)
                .onErrorMap(e -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 인증 서버 요청에 실패하였습니다."))
                .block();
        return
    }
}
