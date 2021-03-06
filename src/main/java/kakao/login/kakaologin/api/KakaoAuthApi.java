package kakao.login.kakaologin.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.login.kakaologin.api.response.GetMemberInfoResponse;
import kakao.login.kakaologin.api.response.GetTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class KakaoAuthApi {

    private final WebClient authApi;
    private final WebClient serviceApi;
    private final ObjectMapper objectMapper;

    private static final String REST_API_APP_KEY = "6971f0ce92ee7c72df40efd0dc64fec7";
    private static final String CLIENT_SECRET = "mdWizUsYCJGYeLvJSvxeg4eVK6k1yS39";

    private static final String GET_TOKEN_URL = "/oauth/token";
    private static final String GET_MEMBER_INFO_URL = "/v2/user/me";




    public KakaoAuthApi(ObjectMapper objectMapper) {
        this.authApi = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        this.objectMapper = objectMapper;

        this.serviceApi = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    public GetTokenResponse getToken(String code, String redirectUrl){
        return authApi.post()
                .uri(GET_TOKEN_URL)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", REST_API_APP_KEY)
                        .with("redirect_uri", redirectUrl)
                        .with("code", code)
                        .with("client_secret", CLIENT_SECRET)
                ).retrieve()
                .bodyToFlux(GetTokenResponse.class)
                .onErrorMap(e -> {
                    log.error("????????? ?????? ?????? ????????? ?????????????????????.", e);
                    return new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "????????? ?????? ?????? ????????? ?????????????????????.");
                })
                .blockLast();
    }

    public GetMemberInfoResponse getMemberInfo(String accessToken) {
        return serviceApi.get()
                .uri(GET_MEMBER_INFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GetMemberInfoResponse.class)
                .onErrorMap(e -> {
                    log.error("????????? ????????? ?????? ????????? ?????????????????????.", e);
                    return new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "????????? ????????? ?????? ????????? ?????????????????????.");
                })
                .blockLast();
    }
}
