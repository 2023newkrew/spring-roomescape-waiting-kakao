package roomescape.nextstep.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @Pattern(regexp = "\\d{3}-\\{d}{4}-\\d{4}")
    private String phone;
    @NotBlank
    private String role;

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}
