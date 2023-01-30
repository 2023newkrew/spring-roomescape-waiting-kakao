package nextstep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
 1. TODO : RoleTypes 또는 roles Immuatable 적용
 2. TODO : Entity, Dto NotNull/Nullable 적용
 3. TODO : Service @Transactional 적
 4. TODO : member_role, reservation 테이블의 member_name 컬럼명 -> member_id
*/

@SpringBootApplication
@ComponentScan(basePackages = {"nextstep", "auth.config"})
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}