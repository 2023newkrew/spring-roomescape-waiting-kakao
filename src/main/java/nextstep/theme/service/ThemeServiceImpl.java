package nextstep.theme.service;

import lombok.RequiredArgsConstructor;
import nextstep.theme.domain.ThemeEntity;
import nextstep.theme.exception.ThemeErrorMessage;
import nextstep.theme.exception.ThemeException;
import nextstep.theme.repository.ThemeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository repository;

    @Transactional
    @Override
    public ThemeEntity create(ThemeEntity theme) {
        return tryInsert(theme);
    }

    private ThemeEntity tryInsert(ThemeEntity theme) {
        try {
            return repository.insert(theme);
        }
        catch (DuplicateKeyException ignore) {
            throw new ThemeException(ThemeErrorMessage.CONFLICT);
        }
    }

    @Override
    public ThemeEntity getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<ThemeEntity> getAll() {
        return repository.getAll();
    }

    @Transactional
    @Override
    public ThemeEntity update(Long id, ThemeEntity theme) {
        if (!tryUpdate(id, theme)) {
            throw new ThemeException(ThemeErrorMessage.NOT_EXISTS);
        }

        return getById(id);
    }

    private boolean tryUpdate(Long id, ThemeEntity theme) {
        try {
            return repository.update(id, theme);
        }
        catch (DuplicateKeyException ignore) {
            throw new ThemeException(ThemeErrorMessage.CONFLICT);
        }
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
