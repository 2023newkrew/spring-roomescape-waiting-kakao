package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.utils.batch.Job;
import nextstep.utils.batch.ReservationSalesStatisticsJobConfig;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class ReservationSalesStatisticsService {

    private final ReservationSalesStatisticsJobConfig jobConfig;

    public void calculateSalesStatistics() {
        LocalDateTime yesterday = LocalDateTime.of(LocalDate.now()/*.minusDays(1)*/, LocalTime.MIN);
        jobConfig.set(yesterday);
        Job job = jobConfig.transitionReservationStatusJob();
        job.runWithCallback(jobConfig::saveStatistics);
    }

}
