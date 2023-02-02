package com.nextstep.domains.member;

import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.member.dtos.MemberRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public MemberEntity findById(Long id) {
        return memberDao.findById(id);
    }
}
