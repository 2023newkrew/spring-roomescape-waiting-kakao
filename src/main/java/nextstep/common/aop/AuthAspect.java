package nextstep.common.aop;

import lombok.extern.slf4j.Slf4j;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.member.Role;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthAspect {
    @Pointcut("@annotation(nextstep.common.annotation.AdminRequired)")
    private void adminRequired() {}

    @Before("adminRequired() && args(member, ..)")
    public void beforeAdminRequired(JoinPoint joinPoint, final Member member) {
        if (member.getRole() != Role.ADMIN) {
            throw new RoomReservationException(ErrorCode.ADMIN_AUTHORITY_REQUIRED);
        }
    }
}
