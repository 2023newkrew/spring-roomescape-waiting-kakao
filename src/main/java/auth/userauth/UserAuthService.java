package auth.userauth;

import auth.AuthenticationException;
import org.springframework.stereotype.Service;

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
            UserAuth userAuth = userAuthRepository.findByUsername(username);
            System.out.println(userAuth.getUsername());
            return userAuth;
        } catch(Exception e){
            throw new AuthenticationException();
        }
    }

    public UserAuth loadUserById(Long id) {
        return userAuthRepository.findById(id);
    }
}
