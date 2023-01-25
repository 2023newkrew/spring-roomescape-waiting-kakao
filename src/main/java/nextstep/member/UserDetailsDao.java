package nextstep.member;

import auth.UserDetails;
import auth.UserDetailsRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDao implements UserDetailsRepository {

    private final MemberDao memberDao;

    public UserDetailsDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails findById(Long id){
        return createUserDetails(memberDao.findById(id));
    }

    @Override
    public UserDetails findByUsername(String username){
        return createUserDetails(memberDao.findByUsername(username));
    }

    private UserDetails createUserDetails(Member member){
        return new UserDetails(
                member.getId(),
                member.getPassword(),
                member.getRole()
        );
    }
}
