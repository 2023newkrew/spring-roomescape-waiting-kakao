package nextstep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SaleHistory {
    private Long id;
    private String themeName;
    private int themePrice;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    private String memberUsername;
    private String memberPhone;
    private long reservationId;

    public SaleHistory(String themeName, int themePrice, LocalDate scheduleDate, LocalTime scheduleTime, String memberUsername, String memberPhone, long reservationId) {
        this.themeName = themeName;
        this.themePrice = themePrice;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.memberUsername = memberUsername;
        this.memberPhone = memberPhone;
        this.reservationId = reservationId;
    }
}
