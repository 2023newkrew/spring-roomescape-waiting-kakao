package nextstep.schedule.service;

import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ScheduleException;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.mapper.ScheduleMapper;
import nextstep.schedule.repository.ScheduleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;

    private final ScheduleMapper mapper;

    @Transactional
    @Override
    public ScheduleResponse create(ScheduleRequest request) {
        Schedule schedule = mapper.fromRequest(request);

        return mapper.toResponse(tryInsert(schedule));
    }

    @Override
    public ScheduleResponse getById(Long id) {
        return mapper.toResponse(repository.getById(id));
    }

    private Schedule tryInsert(Schedule schedule) {
        try {
            return repository.insert(schedule);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ScheduleException(ErrorMessage.SCHEDULE_CONFLICT);
        }
    }

    @Override
    public List<ScheduleResponse> getByThemeIdAndDate(Long themeId, LocalDate date) {
        return repository.getByThemeIdAndDate(themeId, Date.valueOf(date))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
