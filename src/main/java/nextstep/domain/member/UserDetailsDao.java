package nextstep.domain.member;

import auth.domain.UserDetails;
import auth.domain.UserDetailsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsDao implements UserDetailsRepository {

    private final MemberDao memberDao;

    public UserDetailsDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<UserDetails> findByUsername(String username){
        return Optional.ofNullable(memberDao.findByUsername(username)
                .map(this::createUserDetails)
                .orElseGet(() -> null)
        );
    }

    private UserDetails createUserDetails(Member member){
        return new UserDetails(
                member.getId(),
                member.getPassword(),
                member.getRole()
        );
    }
}
