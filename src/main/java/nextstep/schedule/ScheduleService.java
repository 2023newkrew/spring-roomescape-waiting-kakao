package nextstep.schedule;

import nextstep.support.NotExistEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeService themeService;

    public ScheduleService(ScheduleDao scheduleDao, ThemeService themeService) {
        this.scheduleDao = scheduleDao;
        this.themeService = themeService;
    }

    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeService.findById(scheduleRequest.getThemeId()).orElseThrow(NotExistEntityException::new);
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public Optional<Schedule> findById(Long id) {
        Schedule schedule = scheduleDao.findById(id).orElseThrow(NotExistEntityException::new);
        return Optional.of(Schedule.builder()
                .id(id)
                .theme(themeService.findById(schedule.getTheme().getId()).orElseThrow(NotExistEntityException::new))
                .date(schedule.getDate())
                .time(schedule.getTime())
                .build());
    }

    public List<Schedule> findAllByThemeIdAndDate(Long themeId, String date) {
        List<Schedule> schedules = scheduleDao.findByThemeIdAndDate(themeId, date);
        return schedules.stream()
                .map(schedule -> Schedule.builder()
                        .id(schedule.getId())
                        .theme(themeService.findById(schedule.getTheme().getId()).orElseThrow(NotExistEntityException::new))
                        .date(schedule.getDate())
                        .time(schedule.getTime())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        scheduleDao.deleteById(id);
    }
}
