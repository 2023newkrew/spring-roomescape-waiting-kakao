package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.support.InvalidMemberException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }


    public String createToken(TokenRequest request) {
        try{
            Member member = memberDao.findByUsername(request.getUsername());
            if(member.checkWrongPassword(request.getPassword())){
                throw new InvalidMemberException();
            }
            return jwtTokenProvider.createToken(member.getId().toString());
        }catch (EmptyResultDataAccessException exception){
            throw new InvalidMemberException();
        }
    }
}
