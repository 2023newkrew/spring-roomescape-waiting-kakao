package com.nextstep.domains.schedule;

import com.nextstep.interfaces.exceptions.ErrorMessageType;
import com.nextstep.interfaces.exceptions.ScheduleException;
import com.nextstep.interfaces.exceptions.ThemeException;
import com.nextstep.interfaces.schedule.dtos.ScheduleRequest;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;
import com.nextstep.interfaces.schedule.dtos.ScheduleMapper;
import com.nextstep.domains.theme.Theme;
import com.nextstep.domains.theme.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService{

    private final ScheduleRepository repository;

    private final ScheduleMapper mapper;

    private final ThemeRepository themeRepository;

    @Transactional
    public ScheduleResponse create(ScheduleRequest request) {
        Theme theme = themeRepository.getById(request.getThemeId());
        if (Objects.isNull(theme)) {
            throw new ThemeException(ErrorMessageType.THEME_NOT_EXISTS);
        }
        Schedule schedule = mapper.fromRequest(request);

        return mapper.toResponse(tryInsert(schedule));
    }

    private Schedule tryInsert(Schedule schedule) {
        try {
            return repository.insert(schedule);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ScheduleException(ErrorMessageType.SCHEDULE_CONFLICT);
        }
    }

    public ScheduleResponse getById(Long id) {
        return mapper.toResponse(repository.getById(id));
    }

    public List<ScheduleResponse> getByThemeIdAndDate(Long themeId, LocalDate date) {
        return repository.getByThemeIdAndDate(themeId, Date.valueOf(date))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
