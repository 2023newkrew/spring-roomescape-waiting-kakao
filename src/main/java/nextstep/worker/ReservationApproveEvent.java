package nextstep.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@AllArgsConstructor
public class ReservationApproveEvent extends ApplicationEvent {
    private Boolean isApprove;

    public Boolean isApproveTrue() {
        return isApprove.equals(true);
    }

    @Override
    void callBack() {
        log.info("{} : 예약금 이력 로그가 남겨졌습니다.", Thread.currentThread().getName());
    }
}
