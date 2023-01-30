package auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserDetailService {
    private final MemberDao memberDao;
    Map<Long, UserDetails> cache = new ConcurrentHashMap<>();

    public UserDetailService(MemberDao memberDao){
        this.memberDao = memberDao;
    }

    public UserDetails loadUserByUsername(String username){
        try {
            Member member = memberDao.findByUsername(username);
            UserDetails userDetails = new UserDetails(member.getId(), member.getUsername(), member.getPassword(), member.getRole());
            cache.put(userDetails.getId(), userDetails);
            return userDetails;
        } catch(Exception e){
            throw new AuthenticationException();
        }
    }

    public UserDetails loadUserById(Long id) {
        return cache.get(id);
    }
}
