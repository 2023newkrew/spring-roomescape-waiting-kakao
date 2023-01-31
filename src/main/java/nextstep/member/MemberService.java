package nextstep.member;

import lombok.RequiredArgsConstructor;
import nextstep.support.DoesNotExistEntityException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findByUsername(String username) {
        return memberDao.findByUsername(username).orElseThrow(DoesNotExistEntityException::new);
    }

    public Member findByUsernameAndPassword(String username, String password) {
        Member member = findByUsername(username);
        if (member.checkWrongPassword(password)) {
            throw new DoesNotExistEntityException();
        }
        return member;
    }

    public Member findById(Long id) {
        return memberDao.findById(id).orElseThrow(DoesNotExistEntityException::new);
    }
}
