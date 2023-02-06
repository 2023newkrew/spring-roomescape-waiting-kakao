package nextstep.utils.batch.model;

public class SalesHistoryProjection implements PagingModel {

    private Long id;
    private int amount;
    private String themeName;

    public SalesHistoryProjection(Long id, int amount, String themeName) {
        this.id = id;
        this.amount = amount;
        this.themeName = themeName;
    }

    @Override
    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getThemeName() {
        return themeName;
    }
}
