package nextstep.schedule.service;

import nextstep.schedule.domain.Schedule;
import nextstep.schedule.domain.ScheduleEntity;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleEntity create(Schedule schedule);

    ScheduleEntity getById(Long id);

    List<ScheduleEntity> getByThemeIdAndDate(Long themeId, LocalDate date);

    boolean deleteById(Long id);
}
