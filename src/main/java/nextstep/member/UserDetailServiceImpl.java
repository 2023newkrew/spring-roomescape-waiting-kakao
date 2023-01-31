package nextstep.member;

import auth.AuthenticationException;
import auth.UserDetail;
import auth.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final MemberDao memberDao;

    public UserDetail getUserDetailByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        if (member == null) {
            throw new AuthenticationException();
        }

        return UserDetail.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
