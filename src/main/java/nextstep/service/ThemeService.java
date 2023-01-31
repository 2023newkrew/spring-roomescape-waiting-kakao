package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ThemeRequest;
import nextstep.domain.persist.Theme;
import nextstep.repository.ThemeDao;
import nextstep.support.exception.api.NoSuchThemeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeDao themeDao;

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Theme theme = themeDao.findById(id);
        if (theme == null) {
            throw new NoSuchThemeException();
        }

        themeDao.delete(id);
    }
}
