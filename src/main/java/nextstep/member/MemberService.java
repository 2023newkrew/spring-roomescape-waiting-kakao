package nextstep.member;

import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberDao.findById(id).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.MEMBER_NOT_FOUND);
        });
    }
}
