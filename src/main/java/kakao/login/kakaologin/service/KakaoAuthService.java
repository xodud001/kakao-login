package kakao.login.kakaologin.service;

import kakao.login.kakaologin.api.KakaoAuthApi;
import kakao.login.kakaologin.api.response.GetMemberInfoResponse;
import kakao.login.kakaologin.api.response.GetTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    @Value("${kakao.authorize.redirect.url}")
    private String redirectUrl;

    private final KakaoAuthApi api;

    public GetTokenResponse getToken(String code){
        return api.getToken(code, redirectUrl);
    }

    public GetMemberInfoResponse getUserInfo(String accessToken){
        GetMemberInfoResponse memberInfo = api.getMemberInfo(accessToken);
        log.info("Response={}", memberInfo);
        return memberInfo;
    }
}
