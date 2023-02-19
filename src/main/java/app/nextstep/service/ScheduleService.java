package app.nextstep.service;

import app.nextstep.domain.Schedule;
import app.nextstep.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {
    private ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, LocalDate date) {
        return scheduleRepository.findByThemeIdAndDate(themeId, date);
    }

    public Long create(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        scheduleRepository.delete(id);
    }
}
