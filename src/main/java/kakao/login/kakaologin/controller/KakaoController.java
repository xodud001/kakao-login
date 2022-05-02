package kakao.login.kakaologin.controller;

import kakao.login.kakaologin.api.response.GetMemberInfoResponse;
import kakao.login.kakaologin.api.response.GetTokenResponse;
import kakao.login.kakaologin.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/kakao")
@Slf4j
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoAuthService authService;

    @GetMapping("/token")
    public String token(HttpServletRequest request, Model model){
        String code = request.getParameter("code");
        GetTokenResponse response = authService.getToken(code);
        GetMemberInfoResponse userInfo = authService.getUserInfo(response.getAccessToken());
        model.addAttribute("response", response);
        model.addAttribute("userInfo", userInfo);
        return "token";
    }
}
