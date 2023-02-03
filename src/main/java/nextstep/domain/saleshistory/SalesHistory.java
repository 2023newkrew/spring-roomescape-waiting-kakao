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

    public SalesHistory(Long id, Long themeId, int amount, String status) {
        this(themeId, amount, SalesHistoryStatus.valueOf(status));
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getThemeId() {
        return themeId;
    }

    public int getAmount() {
        return amount;
    }

    public SalesHistoryStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
