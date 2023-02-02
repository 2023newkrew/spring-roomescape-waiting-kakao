package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.saleshistory.SalesHistory;
import nextstep.domain.saleshistory.SalesHistoryDao;
import nextstep.domain.saleshistory.SalesHistorySearch;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import nextstep.dto.response.SalesStatisticsResponse;
import nextstep.utils.TransactionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationSalesStatisticsService {

    private static final int CHUNK_SIZE = 20;

    private final SalesHistoryDao salesHistoryDao;
    private final ThemeDao themeDao;
    private final TransactionUtil transactionUtil;

    public SalesStatisticsResponse calculateSalesStatistics() {
        LocalDateTime yesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        SalesHistorySearch salesHistorySearch = new SalesHistorySearch(null, yesterday);
        SalesStatisticsResponse salesStatisticsResponse = new SalesStatisticsResponse();

        while (true) {
            int saleHistoryReadSize = calculateSalesStatisticsByChunkSize(salesHistorySearch, salesStatisticsResponse);
            if (saleHistoryReadSize < CHUNK_SIZE) {
                break;
            }
        }

        return salesStatisticsResponse;
    }

    public int calculateSalesStatisticsByChunkSize(SalesHistorySearch salesHistorySearch, SalesStatisticsResponse salesStatisticsResponse) {
        return transactionUtil.executeTask(() -> {
            List<SalesHistory> salesHistories = readSalesHistories(salesHistorySearch.getLastHistoryId(), salesHistorySearch.getYesterday());
            List<Theme> themes = readThemes(salesHistories);
            process(salesHistories, themes, salesStatisticsResponse);

            salesHistorySearch.setLastHistoryId(salesHistories.get(salesHistories.size() - 1).getId());
            return salesHistories.size();
        }, false);
    }

    private void process(List<SalesHistory> salesHistories, List<Theme> themes, SalesStatisticsResponse salesStatisticsResponse) {
        Map<Long, String> themeMap = themes.stream()
                .collect(Collectors.toMap(Theme::getId, Theme::getName));

        for (SalesHistory salesHistory : salesHistories) {
            salesStatisticsResponse.addTotalSales(salesHistory.getAmount());
            salesStatisticsResponse.addSalesPerTheme(themeMap.get(salesHistory.getThemeId()), salesHistory.getAmount());
        }
    }

    private List<Theme> readThemes(List<SalesHistory> salesHistories) {
        List<Long> themeIds = salesHistories.stream()
                .map(SalesHistory::getThemeId)
                .collect(Collectors.toList());

        return themeDao.findByIds(themeIds);
    }

    private List<SalesHistory> readSalesHistories(Long lastHistoryId, LocalDateTime yesterday) {
        return salesHistoryDao.findHistoryByCreatedAt(lastHistoryId, yesterday, CHUNK_SIZE);
    }

}
