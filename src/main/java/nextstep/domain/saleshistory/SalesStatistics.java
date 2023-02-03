package nextstep.domain.saleshistory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SalesStatistics {

    private int totalSales;
    private Map<String, Integer> salesPerTheme;
    private LocalDateTime dateTime;

    public SalesStatistics(LocalDateTime dateTime) {
        this.totalSales = 0;
        this.salesPerTheme = new HashMap<>();
        this.dateTime = dateTime;
    }

    public void addTotalSales(int sales) {
        totalSales += sales;
    }

    public void addSalesPerTheme(String themeName, int sales) {
        salesPerTheme.put(themeName, salesPerTheme.getOrDefault(themeName, 0) + sales);
    }

    public int getTotalSales() {
        return totalSales;
    }

    public Map<String, Integer> getSalesPerTheme() {
        return salesPerTheme;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
