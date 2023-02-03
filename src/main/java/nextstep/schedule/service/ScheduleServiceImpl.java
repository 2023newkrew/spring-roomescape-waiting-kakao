package nextstep.schedule.service;

import lombok.RequiredArgsConstructor;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.schedule.exception.ScheduleErrorMessage;
import nextstep.schedule.exception.ScheduleException;
import nextstep.schedule.repository.ScheduleRepository;
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
    public ScheduleEntity create(ScheduleEntity schedule) {
        validateTheme(schedule.getThemeId());

        return tryInsert(schedule);
    }

    private void validateTheme(Long themeId) {
        if (Objects.isNull(themeRepository.getById(themeId))) {
            throw new ThemeException(ThemeErrorMessage.NOT_EXISTS);
        }
    }

    private ScheduleEntity tryInsert(ScheduleEntity schedule) {
        try {
            return repository.insert(schedule);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ScheduleException(ScheduleErrorMessage.CONFLICT);
        }
    }

    @Override
    public ScheduleEntity getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<ScheduleEntity> getByThemeIdAndDate(Long themeId, LocalDate date) {
        return repository.getByThemeIdAndDate(themeId, Date.valueOf(date));
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
