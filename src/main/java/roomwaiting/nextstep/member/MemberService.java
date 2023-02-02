package roomwaiting.nextstep.member;

import roomwaiting.auth.userdetail.UserDetails;
import roomwaiting.auth.userdetail.UserDetailsService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static roomwaiting.nextstep.member.Role.ADMIN;
import static roomwaiting.support.Messages.*;

@Service
public class MemberService implements UserDetailsService {
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;
    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Long create(MemberRequest memberRequest) {
        memberDao.findByUsername(memberRequest.getUsername()).ifPresent(val -> {
            throw new DuplicateKeyException(ALREADY_USER.getMessage());
        });
        Member member = new Member(memberRequest.getUsername(), passwordEncoder.encode(memberRequest.getPassword()),
                memberRequest.getName(), memberRequest.getPhone(), memberRequest.getRole());
        return memberDao.save(member);
    }

    public Member findByUsername(String username) {
        Optional<Member> member = memberDao.findByUsername(username);
        return member.orElseThrow(() ->
                new NullPointerException(MEMBER_NOT_FOUND.getMessage() + USERNAME + username));
    }

    public void updateAdmin(MemberRequest memberRequest) {
        Member member = memberDao.findByUsername(memberRequest.getUsername())
                .orElseThrow(() -> new NullPointerException(MEMBER_NOT_FOUND.getMessage() + USERNAME + memberRequest.getUsername()));
        if (Objects.equals(member.getRole(), ADMIN.name())){
            throw new DuplicateKeyException(ALREADY_ADMIN.getMessage());
        }
        memberDao.updateAdmin(member.getUsername());
    }

    public Member findById(Long id) {
        return memberDao.findById(id).orElseThrow(() ->
                new NullPointerException(MEMBER_NOT_FOUND.getMessage() + ID + id)
        );
    }

    @Override
    public UserDetails findUserDetailsByUsername(String username) {
        Member member = memberDao.findByUsername(username).orElseThrow(() ->
                new NullPointerException(MEMBER_NOT_FOUND.getMessage() + USERNAME + username));
        return member.convertToUserDetails();
    }

    @Override
    public UserDetails findUserDetailsById(Long id) {
        Member member = memberDao.findById(id).orElseThrow(() ->
                new NullPointerException(MEMBER_NOT_FOUND.getMessage() + ID + id));
        return member.convertToUserDetails();
    }
}
