package nextstep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
 1. TODO : RoleTypes 또는 roles Immuatable 적용
 2. TODO : Entity, Dto NotNull/Nullable 적용
 3. TODO : member_role, reservation 테이블의 member_name 컬럼명 -> member_id
 4. TODO : 피드백 적용 (https://github.com/next-step/spring-roomesacpe-auth-kakao/pull/65#discussion_r1086217086)
 5. TODO : 예약 취소 시, 예약 대기를 예약 테이블로 이동
 6. TODO : NullPointException -> NotExistEntityException
*/

@SpringBootApplication
@ComponentScan(basePackages = {"nextstep", "auth.config"})
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}