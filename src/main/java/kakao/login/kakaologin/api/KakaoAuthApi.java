package kakao.login.kakaologin.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.login.kakaologin.api.response.GetMemberInfoResponse;
import kakao.login.kakaologin.api.response.GetTokenResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class KakaoAuthApi {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String REST_API_APP_KEY = "6971f0ce92ee7c72df40efd0dc64fec7";
    private static final String CLIENT_SECRET = "mdWizUsYCJGYeLvJSvxeg4eVK6k1yS39";

    private static final String GET_TOKEN_URL = "/oauth/token";
    private static final String GET_MEMBER_INFO_URL = "/v2/user/me";



    public KakaoAuthApi(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    public GetTokenResponse getToken(String code, String redirectUrl){
        return webClient.post()
                .uri(GET_TOKEN_URL)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", REST_API_APP_KEY)
                        .with("redirect_uri", redirectUrl)
                        .with("code", code)
                        .with("client_secret", CLIENT_SECRET)
                ).retrieve()
                .bodyToFlux(GetTokenResponse.class)
                .onErrorMap(e -> {
                    log.error("카카오 인증 서버 요청에 실패하였습니다.", e);
                    return new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 인증 서버 요청에 실패하였습니다.");
                })
                .blockLast();
    }

    public GetMemberInfoResponse getMemberInfo(String accessToken){
        return webClient.post()
                .uri(GET_MEMBER_INFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GetMemberInfoResponse.class)
                .onErrorMap(e -> {
                    log.error("카카오 사용자 정보 조회에 실패하였습니다.", e);
                    return new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 정보 조회에 실패하였습니다.");
                })
                .blockLast();
    }
}
