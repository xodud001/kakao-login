package kakao.login.kakaologin.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientDecorator {

    private final WebClient webClient;

    public WebClientDecorator(){
        //TODO: URL 외부에서 주입 할 수 있도록 수정해야됨
        this.webClient = WebClient.builder()
                .baseUrl("http://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    public String callApiServer(String uri, Object json){
        return webClient.post()
                .uri(uri)
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("ERROR")
                .block();
    }
}
