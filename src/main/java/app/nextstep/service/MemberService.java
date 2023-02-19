package app.nextstep.service;

import app.nextstep.domain.Member;
import app.nextstep.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long create(Member member) {
        return memberRepository.save(member);
    }
}
