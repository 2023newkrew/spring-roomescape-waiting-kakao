package nextstep.utils.batch.processor;

import nextstep.domain.saleshistory.SalesStatistics;
import nextstep.utils.batch.model.SalesHistoryProjection;

public class SalesStatisticsProcessor implements Processor<SalesHistoryProjection, SalesStatistics> {

    private SalesStatistics salesStatistics;

    public SalesStatisticsProcessor(SalesStatistics salesStatistics) {
        this.salesStatistics = salesStatistics;
    }

    @Override
    public SalesStatistics process(SalesHistoryProjection item) {
        salesStatistics.addTotalSales(item.getAmount());
        salesStatistics.addSalesPerTheme(item.getThemeName(), item.getAmount());

        return salesStatistics;
    }
}
