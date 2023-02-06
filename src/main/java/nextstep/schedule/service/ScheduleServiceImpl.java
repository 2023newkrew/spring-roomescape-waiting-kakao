package nextstep.schedule.service;

import lombok.RequiredArgsConstructor;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.exception.ScheduleErrorMessage;
import nextstep.schedule.exception.ScheduleException;
import nextstep.schedule.repository.ScheduleRepository;
import nextstep.theme.domain.Theme;
import nextstep.theme.exception.ThemeErrorMessage;
import nextstep.theme.exception.ThemeException;
import nextstep.theme.repository.ThemeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;

    private final ThemeRepository themeRepository;

    @Transactional
    @Override
    public Schedule create(Schedule schedule) {
        Theme theme = themeRepository.getById(schedule.getThemeId());
        validateTheme(theme);

        return tryInsert(schedule);
    }

    private void validateTheme(Theme theme) {
        if (Objects.isNull(theme)) {
            throw new ThemeException(ThemeErrorMessage.NOT_EXISTS);
        }
    }

    private Schedule tryInsert(Schedule schedule) {
        try {
            return repository.insert(schedule);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ScheduleException(ScheduleErrorMessage.CONFLICT);
        }
    }

    @Override
    public Schedule getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Schedule> getAllByThemeAndDate(Theme theme, LocalDate date) {
        return repository.getAllByThemeAndDate(theme, Date.valueOf(date));
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
