<<<<<<<< HEAD:src/main/java/nextstep/domain/dto/request/MemberRequest.java
package nextstep.domain.dto.request;
========
package nextstep.domain.dto;
>>>>>>>> 59193cb (refactor: 패키지 구조 변경):src/main/java/nextstep/domain/dto/MemberRequest.java

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.domain.persist.Member;

@Getter
@AllArgsConstructor
public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}
