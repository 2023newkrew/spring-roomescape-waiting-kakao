package nextstep.auth.model;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class TokenRequest {
    @NotBlank
    private String memberName;

    @NotBlank
    private String password;

    public TokenRequest(String memberName, String password) {
        this.memberName = memberName;
        this.password = password;
    }
}
