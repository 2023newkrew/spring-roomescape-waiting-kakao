package nextstep.common;

import java.util.concurrent.atomic.AtomicInteger;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;

public class Lock {
    private final AtomicInteger lock = new AtomicInteger(0);

    public void lock() {
        while(!lock.compareAndSet(0, 1)) {}
    }

    public void unlock() {
        if (!lock.compareAndSet(1, 0)) {
            throw new RoomReservationException(ErrorCode.INVALID_UNLOCK);
        };
    }
}
