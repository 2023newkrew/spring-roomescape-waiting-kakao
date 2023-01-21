package nextstep.member.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class MemberRequest {

    @NotBlank(message = "유저이름은 공백일 수 없습니다.")
    private final String username;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private final String password;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private final String name;

    @NotBlank(message = "전화번호는 공백일 수 없습니다.")
    private final String phone;

    public MemberRequest() {
        this(null, null, null, null);
    }
}
