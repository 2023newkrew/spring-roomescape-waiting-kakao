package auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenRequest {
    @Schema(description = "유저 아이디")
    private String username;
    @Schema(description = "유저 패스워드")
    private String password;
}
