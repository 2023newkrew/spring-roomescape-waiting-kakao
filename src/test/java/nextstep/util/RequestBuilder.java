package nextstep.util;

import auth.domain.dto.request.TokenRequest;
import lombok.experimental.UtilityClass;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.request.ScheduleRequest;
import nextstep.domain.dto.request.ThemeRequest;
import org.springframework.context.annotation.Profile;

@UtilityClass
@Profile("test")
public class RequestBuilder {
    public static MemberRequest memberRequestForAdmin() {
        return MemberRequest.builder()
                .username("adminId")
                .password("adminPassword")
                .name("adminName")
                .phone("010-1234-5678")
                .role("ADMIN")
                .build();
    }

    public static MemberRequest memberRequestForUser() {
        return MemberRequest.builder()
                .username("userId")
                .password("userPassword")
                .name("userName")
                .phone("010-2345-6789")
                .role("USER")
                .build();
    }

    public static TokenRequest tokenRequestForAdmin() {
        return TokenRequest.builder()
                .username("adminId")
                .password("adminPassword")
                .build();
    }

    public static TokenRequest tokenRequestForUser() {
        return TokenRequest.builder()
                .username("userId")
                .password("userPassword")
                .build();
    }

    public static ThemeRequest themeRequest() {
        return ThemeRequest.builder()
                .name("테마 이름")
                .desc("테마 설명")
                .price(22000)
                .build();
    }

    public static ScheduleRequest scheduleRequest(Long themeId) {
        return ScheduleRequest.builder()
                .themeId(themeId)
                .date("2022-08-11")
                .time("13:00")
                .build();
    }

    public static ReservationRequest reservationRequest(Long scheduleId) {
        return ReservationRequest.builder()
                .scheduleId(scheduleId)
                .build();
    }
}
