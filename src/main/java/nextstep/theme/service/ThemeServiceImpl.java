package nextstep.theme.service;

import lombok.RequiredArgsConstructor;
import nextstep.theme.domain.Theme;
import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;
import nextstep.theme.exception.ThemeErrorMessage;
import nextstep.theme.exception.ThemeException;
import nextstep.theme.mapper.ThemeMapper;
import nextstep.theme.repository.ThemeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository repository;

    private final ThemeMapper mapper;

    @Transactional
    @Override
    public ThemeResponse create(ThemeRequest request) {
        var theme = mapper.fromRequest(request);
        try {
            theme = repository.insert(theme);
        }
        catch (DuplicateKeyException ignore) {
            throw new ThemeException(ThemeErrorMessage.CONFLICT);
        }

        return mapper.toResponse(theme);
    }

    @Override
    public ThemeResponse getById(Long id) {
        Theme theme = repository.getById(id);

        return mapper.toResponse(theme);
    }

    @Override
    public List<ThemeResponse> getAll() {
        return repository.getAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ThemeResponse update(Long id, ThemeRequest request) {
        Theme theme = mapper.fromRequest(request);
        boolean updated;
        try {
            updated = repository.update(id, theme);
        }
        catch (DuplicateKeyException ignore) {
            throw new ThemeException(ThemeErrorMessage.CONFLICT);
        }
        if (!updated) {
            throw new ThemeException(ThemeErrorMessage.NOT_EXISTS);
        }

        return getById(id);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try {
            return repository.delete(id);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ThemeException(ThemeErrorMessage.RESERVATION_EXISTS);
        }
    }
}
