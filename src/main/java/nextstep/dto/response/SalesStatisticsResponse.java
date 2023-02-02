package nextstep.dto.response;

import java.util.HashMap;
import java.util.Map;

public class SalesStatisticsResponse {

    private int totalSales;
    private Map<String, Integer> salesPerTheme;

    public SalesStatisticsResponse() {
        this.totalSales = 0;
        this.salesPerTheme = new HashMap<>();
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
}
