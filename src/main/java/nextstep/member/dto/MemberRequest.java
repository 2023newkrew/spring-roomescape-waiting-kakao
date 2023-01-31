package nextstep.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberRequest {

    @NotBlank(message = "username은 공백일 수 없습니다.")
    private String username;

    @NotBlank(message = "password는 공백일 수 없습니다.")
    private String password;

    @NotBlank(message = "name은 공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "phone은 공백일 수 없습니다.")
    private String phone;
}
