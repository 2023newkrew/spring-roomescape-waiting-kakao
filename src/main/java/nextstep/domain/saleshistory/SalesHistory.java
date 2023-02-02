package nextstep.domain.saleshistory;

import java.time.LocalDateTime;

public class SalesHistory {

    private Long id;
    private Long themeId;
    private int amount;
    private SalesHistoryStatus status;
    private LocalDateTime createdAt;

    public SalesHistory(Long themeId, int amount, SalesHistoryStatus status) {
        this.themeId = themeId;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

}
