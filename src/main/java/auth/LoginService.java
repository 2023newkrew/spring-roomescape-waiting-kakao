package auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsRepository userDetailsRepository;
    private final Map<Long, UserDetails> cache = new ConcurrentHashMap<>();

    public LoginService(final JwtTokenProvider jwtTokenProvider, final UserDetailsRepository userDetailsRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsRepository = userDetailsRepository;
    }

    /* member로 향하는 의존을 제거 */
    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsRepository.findByUsername(tokenRequest.getUsername());
            cache.put(userDetails.getId(), userDetails);
        } catch(Exception e){
            throw new AuthenticationException();
        }

        if (userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    /* DB에 접근하지 않기 위해 cache 사용 */
    public UserDetails extractMember(String credential) {
        Long id = extractPrincipal(credential);
        return cache.get(id);
    }
}
