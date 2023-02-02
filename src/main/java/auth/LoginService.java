package auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsRepository userDetailsRepository;
    private final Map<Long, UserDetails> cache = new ConcurrentHashMap<>(); //사용자 정보 캐시값
    private final Map<Long, Long> cacheLoadTime = new ConcurrentHashMap<>(); //캐시가 로드된 시간
    private final long cacheMaxAge = 60 * 1000L; //1분 설정


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
            cacheLoadTime.put(userDetails.getId(), System.currentTimeMillis());
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

    /* DB에 접근하지 않기 위해 cache 사용 (lazy loading) */
    public UserDetails extractMember(String credential) {
        Long id = extractPrincipal(credential);
        long now = System.currentTimeMillis();
        /* 캐시가 만료되면 제거 */
        if (now > cacheLoadTime.get(id) + cacheMaxAge) {
            cache.remove(id);
        }
        /* 캐시가 히트됐을 경우 */
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        /* 캐시가 히트되지 않았을 경우 */
        UserDetails userDetails = userDetailsRepository.findById(id);
        cache.put(id, userDetails);
        cacheLoadTime.put(userDetails.getId(), System.currentTimeMillis());
        return userDetails;
    }
}
