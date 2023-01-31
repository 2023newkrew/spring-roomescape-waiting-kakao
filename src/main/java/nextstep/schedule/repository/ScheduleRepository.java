package nextstep.schedule.repository;

import nextstep.schedule.domain.ScheduleEntity;

import java.sql.Date;
import java.util.List;

public interface ScheduleRepository {

    ScheduleEntity insert(ScheduleEntity schedule);

    ScheduleEntity getById(Long id);

    List<ScheduleEntity> getByThemeIdAndDate(Long themeId, Date date);

    boolean deleteById(Long id);
}
