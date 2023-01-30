package nextstep.service;

import nextstep.domain.dto.request.ThemeRequest;
import nextstep.domain.persist.Theme;
import nextstep.repository.ThemeDao;
import nextstep.support.exception.api.NoSuchThemeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThemeService {
    private ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

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
