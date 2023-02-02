package nextstep.domain.saleshistory;

import java.time.LocalDateTime;

public class SalesHistorySearch {

    private Long lastHistoryId;
    private LocalDateTime yesterday;

    public SalesHistorySearch(Long lastHistoryId, LocalDateTime yesterday) {
        this.lastHistoryId = lastHistoryId;
        this.yesterday = yesterday;
    }

    public void setLastHistoryId(Long lastHistoryId) {
        this.lastHistoryId = lastHistoryId;
    }

    public Long getLastHistoryId() {
        return lastHistoryId;
    }

    public LocalDateTime getYesterday() {
        return yesterday;
    }
}
