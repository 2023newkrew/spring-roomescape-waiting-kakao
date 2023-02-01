package com.nextstep.domains.theme;

import com.nextstep.domains.exceptions.ErrorMessageType;
import com.nextstep.domains.exceptions.ThemeException;
import com.nextstep.interfaces.theme.dtos.ThemeRequest;
import com.nextstep.interfaces.theme.dtos.ThemeResponse;
import com.nextstep.interfaces.theme.dtos.ThemeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ThemeService{

    private final ThemeRepository repository;

    private final ThemeMapper mapper;

    @Transactional
    public ThemeResponse create(ThemeRequest request) {
        var theme = mapper.fromRequest(request);
        try {
            theme = repository.insert(theme);
        }
        catch (DuplicateKeyException ignore) {
            throw new ThemeException(ErrorMessageType.THEME_CONFLICT);
        }

        return mapper.toResponse(theme);
    }

    public ThemeResponse getById(Long id) {
        Theme theme = repository.getById(id);

        return mapper.toResponse(theme);
    }

    public List<ThemeResponse> getAll() {
        return repository.getAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ThemeResponse update(Long id, ThemeRequest request) {
        Theme theme = mapper.fromRequest(request);
        boolean updated;
        try {
            updated = repository.update(id, theme);
        }
        catch (DuplicateKeyException ignore) {
            throw new ThemeException(ErrorMessageType.THEME_CONFLICT);
        }
        if (!updated) {
            throw new ThemeException(ErrorMessageType.THEME_NOT_EXISTS);
        }

        return getById(id);
    }

    @Transactional
    public boolean deleteById(Long id) {
        try {
            return repository.delete(id);
        }
        catch (DataIntegrityViolationException ignore) {
            throw new ThemeException(ErrorMessageType.THEME_RESERVATION_EXISTS);
        }
    }
}
