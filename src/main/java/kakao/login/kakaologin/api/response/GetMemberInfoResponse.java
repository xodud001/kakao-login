package kakao.login.kakaologin.api.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kakao.login.kakaologin.api.KakaoAccount;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetMemberInfoResponse {
    private String id;
    private Boolean hasSignedUp;
    private LocalDateTime connectedAt;
    private LocalDateTime synchedAt;
    private PropertiesResponse properties;
    private KakaoAccount kakaoAccount;

}
