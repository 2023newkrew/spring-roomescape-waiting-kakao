package auth.userauth;

import auth.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * UserAuth와 관련된 로직을 처리하는 클래스.<br><br>
 *
 * nextstep.member 패키지(MemberService, Member)에서 이 클래스를 의존하여 <br>Repository에 멤버에 대한 인증 정보(UserAuth)를 저장한다.
 */
@Service
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;
    public UserAuthService(UserAuthRepository userAuthRepository){
        this.userAuthRepository = userAuthRepository;
    }

    public void addUserAuth(UserAuth userAuth){
        userAuthRepository.addUserAuth(userAuth);
    }

    public UserAuth loadUserByUsername(String username){
        try {
            return userAuthRepository.findByUsername(username);
        } catch(Exception e){
            throw new AuthenticationException();
        }
    }

    public UserAuth loadUserById(Long id) {
        return userAuthRepository.findById(id);
    }
}
