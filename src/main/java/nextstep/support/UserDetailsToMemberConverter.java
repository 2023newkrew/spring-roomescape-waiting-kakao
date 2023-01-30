package nextstep.support;

import auth.domain.persist.UserDetails;
import nextstep.domain.persist.Member;
import org.springframework.core.convert.converter.Converter;

@Deprecated
public class UserDetailsToMemberConverter implements Converter<UserDetails, Member> {
    @Override
    public Member convert(UserDetails userDetails) {
        return new Member(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getPassword(),
            userDetails.getName(),
            userDetails.getPhone(),
            userDetails.getRole()
        );
    }
}
