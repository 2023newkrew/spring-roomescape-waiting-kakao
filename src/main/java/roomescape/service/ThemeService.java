package roomescape.service;

import errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeControllerPostBody;
import roomescape.entity.Theme;
import roomescape.exception.ServiceException;
import roomescape.repository.ThemeRepository;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class ThemeService {
    private final ThemeRepository repository;

    @Transactional(readOnly = true)
    public Theme getTheme(long id) {
        var target = repository.selectById(id);
        if (target.isEmpty()) {
            throw new ServiceException(ErrorCode.UNKNOWN_THEME_ID);
        }
        return target.get();
    }


    public long createTheme(ThemeControllerPostBody body) {
        return repository.insert(body.getName(), body.getDesc(), body.getPrice());
    }

    @Transactional(readOnly = true)
    public Stream<Theme> pageTheme(int page) {
        return repository.selectPage(30, Math.max(page, 0))
                         .stream();
    }

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
