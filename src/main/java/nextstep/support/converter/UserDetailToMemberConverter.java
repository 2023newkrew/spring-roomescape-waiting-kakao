package nextstep.support.converter;

import auth.domain.persist.UserDetails;
import lombok.experimental.UtilityClass;
import nextstep.domain.persist.Member;

@UtilityClass
public class UserDetailToMemberConverter {
    public Member convertUserDetailToMember(UserDetails userDetails) {
        return new Member(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getName(),
                userDetails.getPhone(),
                userDetails.getPhone()
        );
    }
}
