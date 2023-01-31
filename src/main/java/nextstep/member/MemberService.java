package nextstep.member;

import lombok.RequiredArgsConstructor;
import nextstep.exception.MemberException;
import nextstep.exception.RoomEscapeExceptionCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findByUsername(String username) {
        return memberDao.findByUsername(username).orElseThrow(() -> new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member findByUsernameAndPassword(String username, String password) {
        Member member = findByUsername(username);
        if (member.checkWrongPassword(password)) {
            throw new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    public Member findById(Long id) {
        return memberDao.findById(id).orElseThrow(() -> new MemberException(RoomEscapeExceptionCode.MEMBER_NOT_FOUND));
    }
}
