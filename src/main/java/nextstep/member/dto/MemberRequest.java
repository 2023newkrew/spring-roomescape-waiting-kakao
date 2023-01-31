package nextstep.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class MemberRequest {

    @NotBlank(message = "username은 공백일 수 없습니다.")
    private final String username;

    @NotBlank(message = "password는 공백일 수 없습니다.")
    private final String password;

    @NotBlank(message = "name은 공백일 수 없습니다.")
    private final String name;

    @NotBlank(message = "phone은 공백일 수 없습니다.")
    private final String phone;

    MemberRequest() {
        this(null, null, null, null);
    }
}
