package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeControllerPostBody;
import roomescape.controller.errors.ErrorCode;
import roomescape.entity.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.ServiceException;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository repository;

    public Theme getTheme(long id) {
        var target = repository.selectById(id);
        if (target.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_THEME_ID);
        }
        return target.get();
    }

    @Transactional
    public long createTheme(ThemeControllerPostBody body) {
        return repository.insert(body.getName(), body.getDesc(), body.getPrice());
    }

    public Stream<Theme> pageTheme(int page) {
        return repository.selectPage(30, Math.max(page, 0))
                         .stream();
    }

    @Transactional
    public void deleteTheme(long id) {
        try {
            var affectedRows = repository.delete(id);
            if (affectedRows == 0) {
                throw new ServiceException(ErrorCode.UNKNOWN_THEME_ID);
            }
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(ErrorCode.USING_THEME);
        }
    }
}
